package com.maxlift.presentation.ui.feature.camera

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector

private var offsetX: Float = 0f
private var offsetY: Float = 0f

private var scaleX: Float = 0f
private var scaleY: Float = 0f
private var cRect: RectF? = null

private var objectDetector: ObjectDetector? = null
private var rotated: Boolean = false

@OptIn(ExperimentalGetImage::class)
@Composable
fun TFLiteObjectDetectionScreen() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val boundingBoxState = remember { mutableStateOf<RectF?>(null) }
    val cropRect = remember { mutableStateOf<Rect?>(null) }

    val previewView = remember { PreviewView(context).apply {
        this.scaleType = PreviewView.ScaleType.FIT_CENTER
    } }

    val options = ObjectDetector.ObjectDetectorOptions.builder()
        .setMaxResults(1)
        .setScoreThreshold(0.2f)
        .build()

    try {
        objectDetector = ObjectDetector.createFromFileAndOptions(context, "1.tflite", options)
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    }

    LaunchedEffect(Unit) {
        val cameraProvider = getCameraProvider(context)
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context))
        { thisImageProxy: ImageProxy ->
            val rotationDegrees = thisImageProxy.imageInfo.rotationDegrees
            val image = TensorImage.fromBitmap(thisImageProxy.toBitmap())

            if (image != null) {
                if(rotationDegrees % 180 != 0) {
                    cropRect.value = Rect(0, 0, image.height, image.width)
                    cRect = RectF(cropRect.value)
                    rotated = true
                }else {
                    cropRect.value = thisImageProxy.cropRect
                    cRect = RectF(cropRect.value)
                    rotated = false
                }

                val imageProcessingOptions = ImageProcessingOptions.builder()
                    .setOrientation(getOrientationFromRotation(rotationDegrees))
                    .build()

                val results = objectDetector?.detect(image, imageProcessingOptions)
                if (results != null) {
                    for(item in results) {
                        if(rotated) boundingBoxState.value = rotateBoundingBox(item.boundingBox)
                        else boundingBoxState.value = item.boundingBox
                    }
                }

                thisImageProxy.close()
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

        BoundingBoxOverlay2(boundingBoxState.value, Modifier.fillMaxSize())
    }
}

private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
    return when(rotation) {
        90 -> ImageProcessingOptions.Orientation.TOP_RIGHT
        180 -> ImageProcessingOptions.Orientation.LEFT_BOTTOM
        270 -> ImageProcessingOptions.Orientation.RIGHT_TOP
        else -> ImageProcessingOptions.Orientation.TOP_LEFT
    }
}

private fun rotateBoundingBox(boundingBox: RectF): RectF {
    return RectF(
        boundingBox.top,
        boundingBox.left,
        boundingBox.bottom,
        boundingBox.right
    )
}

/**
 * Draws the given [boundingBox] of the detected object on a Canvas.
 */
@Composable
fun BoundingBoxOverlay2(boundingBox: RectF?, modifier: Modifier) {
    Canvas(modifier = modifier) {
        if(boundingBox != null && cRect != null) {
            val box: RectF = adjustBoundingBox(boundingBox)

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

//private fun flipBoundingBox(boundingBox: RectF, cropRect: RectF?): RectF{
//    val imageWidth = cropRect!!.width()
//
//    return RectF(
//        imageWidth - boundingBox.left,
//        boundingBox.top,
//        imageWidth - boundingBox.right,
//        boundingBox.bottom
//    )
//}

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