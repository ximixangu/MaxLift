package com.maxlift.presentation.ui.feature.camera

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalGetImage::class)
@Composable
fun ObjectDetectionScreen() {
    val context = LocalContext.current

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val lensFacing = remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val boundingBoxState = remember { mutableStateOf<Rect?>(null) }
    val cropRect = remember { mutableStateOf<Rect?>(null) }

    val previewView = remember { PreviewView(context).apply {
        this.scaleType = PreviewView.ScaleType.FIT_CENTER
    } }

    val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .build()
    val objectDetector = ObjectDetection.getClient(options)

    LaunchedEffect(lensFacing.intValue) {
        val cameraProvider = context.getCameraProvider()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing.intValue).build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)
        ) { thisImageProxy: ImageProxy ->
            val rotationDegrees = thisImageProxy.imageInfo.rotationDegrees
            val mediaImage = thisImageProxy.image
            cropRect.value = thisImageProxy.cropRect

            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
                if(rotationDegrees % 180 != 0) {
                    cropRect.value = Rect(0, 0, image.height, image.width)
                }

                objectDetector.process(image)
                    .addOnSuccessListener { detectedObjects ->
                        for (obj in detectedObjects) {
                            boundingBoxState.value = obj.boundingBox
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Analyzer Error", e.message ?: "")
                    }
                    .addOnCompleteListener {
                        thisImageProxy.close()
                    }
            }
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider },
            imageAnalysis
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView( { previewView },
            modifier = Modifier
                .fillMaxSize()
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            BoundingBoxOverlay(
                boundingBoxState.value,
                previewView,
                cropRect.value
            )
        }
    }
}

@OptIn(TransformExperimental::class)
@Composable
fun BoundingBoxOverlay(boundingBox: Rect?, previewView: PreviewView, cropRect: Rect?) {
    Canvas(Modifier.fillMaxSize()) {
        if(boundingBox != null && cropRect != null) {
            val cameraAspectRatio = cropRect.width().toFloat() / cropRect.height()
            val previewViewAspectRatio = previewView.width.toFloat() / previewView.height

            val scaledWidth: Float
            val scaledHeight: Float

            if(cameraAspectRatio > previewViewAspectRatio) {
                scaledWidth = previewView.width.toFloat()
                scaledHeight = scaledWidth / cameraAspectRatio
            }else {
                scaledHeight = previewView.height.toFloat()
                scaledWidth = scaledHeight * cameraAspectRatio
            }

            val offsetX = (previewView.width - scaledWidth) / 2
            val offsetY = (previewView.height - scaledHeight) / 2

            val box = adjustBoundingBox(boundingBox, cropRect, scaledWidth, scaledHeight)

            withTransform({
                translate(offsetX, offsetY)
            }) {
                val topLeft = Offset(box.left, box.top)
                val bottomRight = Offset(box.right, box.bottom)

                drawCircle(center = topLeft, radius = 10f, color = Color.Blue)
                drawCircle(center = Offset(box.right, box.top), radius = 10f,  color = Color.Blue)
                drawCircle(center = Offset(box.left, box.bottom), radius = 10f,  color = Color.Blue)
                drawCircle(center = bottomRight, radius = 10f,  color = Color.Blue)

                drawLine(color = Color.Gray, topLeft, Offset(box.right, box.top))
                drawLine(color = Color.Gray, topLeft, Offset(box.left, box.bottom))
                drawLine(color = Color.Gray, bottomRight, Offset(box.right, box.top))
                drawLine(color = Color.Gray, bottomRight, Offset(box.left, box.bottom))
            }
        }
    }
}

private fun adjustBoundingBox(rect: Rect, cropRect: Rect, scaledWidth: Float, scaledHeight: Float): RectF {
    val scaleX = scaledWidth / cropRect.width()
    val scaleY = scaledHeight / cropRect.height()

    return RectF(
        (rect.left * scaleX),
        (rect.top * scaleY),
        (rect.right * scaleX),
        (rect.bottom * scaleY)
    )
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }
