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
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.maxlift.presentation.ui.feature.calculator.blendColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private var offsetX: Float = 0f
private var offsetY: Float = 0f

private var scaleX: Float = 0f
private var scaleY: Float = 0f

@OptIn(ExperimentalGetImage::class)
@Composable
fun MLKitObjectDetectionScreen(viewModel: CameraViewModel, navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var isProcessingMovement by remember { mutableStateOf(false) }

    var timeColor by remember { mutableStateOf(Color.Green) }
    val times by viewModel.times.observeAsState(null)

    val boundingBoxesStates = remember { mutableStateOf<List<RectF>>(emptyList()) }
    var cropRect by remember { mutableStateOf<Rect?>(null) }

    val previewView = remember { PreviewView(context).apply {
        this.scaleType = PreviewView.ScaleType.FIT_CENTER
    } }

    val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableMultipleObjects()
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
            cropRect = thisImageProxy.cropRect

            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
                if(rotationDegrees % 180 != 0) {
                    cropRect = Rect(0, 0, image.height, image.width)
                }

                objectDetector.process(image)
                    .addOnSuccessListener { detectedObjects ->
                        val boxList = mutableListOf<RectF>()
                        for (obj in detectedObjects.asReversed()) {
                            val ratio = obj.boundingBox.height().toFloat() / obj.boundingBox.width()
                            if(0.8 < ratio && ratio < 1.2) {
                                boxList.add(RectF(obj.boundingBox))
                            }
                        }
                        boundingBoxesStates.value = boxList
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

    LaunchedEffect(boundingBoxesStates.value) {
        if(isProcessingMovement && boundingBoxesStates.value.size == 1) {
            sendToBackgroundProcessing(boundingBoxesStates.value[0], viewModel)
        }
    }

    LaunchedEffect(cropRect) {
        cropRect?.let{
            setDrawingOffsetAndScale(previewView, cropRect!!)
        }
    }

    LaunchedEffect(times?.last()) {
        if(times != null && times?.size!! > 1) {
            timeColor = generateColor(times?.last()!!, times?.get(times?.size!! - 2)!!)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView( { previewView } , Modifier.fillMaxSize())

        if(boundingBoxesStates.value.isNotEmpty()) {
            MultipleBoundingBoxOverlay(boundingBoxesStates.value)
        }

        Column(
            Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.2f))
                    .fillMaxWidth()
                    .padding(20.dp)
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if(times?.isNotEmpty() == true)"${times?.last()} ms" else "",
                    style = MaterialTheme.typography.titleLarge,
                    color = timeColor
                )
            }

            Spacer(Modifier.fillMaxSize().weight(7f))

            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.2f))
                    .fillMaxWidth()
                    .padding(20.dp)
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = isProcessingMovement || boundingBoxesStates.value.isNotEmpty()) { active ->
                    if (active) {
                        RecordButton(size = 80) {
                            isProcessingMovement = !isProcessingMovement
                            if (isProcessingMovement) {
                                viewModel.resetBoundingBoxProcessing()
                            } else {
                                if (times?.isNotEmpty() == true) {
                                    navController.navigate("result")
                                }
                            }
                        }
                    } else {
                        DisabledRecordButton(size = 80)
                    }
                }
            }
        }
    }
}

/**
 * Draws the given [boundingBoxes] of the detected object on a Canvas.
 */
@Composable
fun MultipleBoundingBoxOverlay(boundingBoxes: List<RectF>) {
    Canvas(Modifier.fillMaxSize()) {
        for (boundingBox in boundingBoxes) {
            val box = scaleBoundingBox(boundingBox)

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
private fun scaleBoundingBox(boundingBox: RectF): RectF {
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

private fun generateColor(newTime: Int, previousTime: Int): Color {
    return blendColors(Color.Green, Color.Red, (newTime - previousTime).toFloat() * 2 / newTime)
}

private fun sendToBackgroundProcessing(boundingBox: RectF, viewModel: CameraViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        viewModel.processBoundingBoxPlus(boundingBox)
    }
}

private suspend fun getCameraProvider(context: Context): ProcessCameraProvider {
    return withContext(Dispatchers.IO) {
        ProcessCameraProvider.getInstance(context).get()
    }
}