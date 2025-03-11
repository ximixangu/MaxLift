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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private var offsetX: Float = 0f
private var offsetY: Float = 0f

private var scaleX: Float = 0f
private var scaleY: Float = 0f

@OptIn(ExperimentalGetImage::class)
@Composable
fun MLKitObjectDetectionScreen() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val boundingBoxState = remember { mutableStateOf<RectF?>(null) }
    val cropRect = remember { mutableStateOf<Rect?>(null) }

    val previewView = remember { PreviewView(context).apply {
        this.scaleType = PreviewView.ScaleType.FIT_CENTER
    } }

    val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .build()
    val objectDetector = ObjectDetection.getClient(options)

    LaunchedEffect(Unit) {
        val cameraProvider = getCameraProvider(context)
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

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
                            boundingBoxState.value = RectF(obj.boundingBox)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Analyzer Error", e.message ?: "...")
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

    LaunchedEffect(cropRect.value) {
        cropRect.value?.let{
            setDrawingOffsetAndScale(previewView, cropRect.value!!)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView( { previewView }, modifier = Modifier.fillMaxSize() )

        BoundingBoxOverlay(boundingBoxState.value)
    }
}

/**
 * Draws the given [boundingBox] of the detected object on a Canvas.
 */
@Composable
fun BoundingBoxOverlay(boundingBox: RectF?) {
    Canvas(Modifier.fillMaxSize()) {
        if(boundingBox != null) {
            val box = adjustBoundingBox(boundingBox)

            withTransform({
                translate(offsetX, offsetY)
            }) {
                val topLeft = Offset(box.left, box.top)
                val bottomRight = Offset(box.right, box.bottom)

                drawCircle(center = topLeft, radius = 10f, color = Color.Blue)
                drawCircle(center = Offset(box.right, box.top), radius = 10f,  color = Color.Blue)
                drawCircle(center = Offset(box.left, box.bottom), radius = 10f,  color = Color.Blue)
                drawCircle(center = bottomRight, radius = 10f,  color = Color.Blue)

                drawLine(color = Color.Red, topLeft, Offset(box.right, box.top))
                drawLine(color = Color.Red, topLeft, Offset(box.left, box.bottom))
                drawLine(color = Color.Red, bottomRight, Offset(box.right, box.top))
                drawLine(color = Color.Red, bottomRight, Offset(box.left, box.bottom))
            }
        }
    }
}

/**
 * Scales the given [boundingBox] to be properly sized.
 */
private fun adjustBoundingBox(boundingBox: RectF): RectF {
    return RectF(
        (boundingBox.left * scaleX),
        (boundingBox.top * scaleY),
        (boundingBox.right * scaleX),
        (boundingBox.bottom * scaleY)
    )
}

/**
 * Computes and stores the Offset and Scale of the camera based
 * on the aspect ratio of the given [previewView] and [cropRect].
 * This function assumes the camera is set to FIT_CENTER.
 * @param cropRect The rectangle defining the camera area.
 */
private fun setDrawingOffsetAndScale(previewView: PreviewView, cropRect: Rect) {
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

    offsetX = (previewView.width - scaledWidth) / 2
    offsetY = (previewView.height - scaledHeight) / 2

    scaleX = scaledWidth / cropRect.width()
    scaleY = scaledHeight / cropRect.height()
}

private suspend fun getCameraProvider(context: Context): ProcessCameraProvider {
    return withContext(Dispatchers.IO) {
        ProcessCameraProvider.getInstance(context).get()
    }
}