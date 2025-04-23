package com.maxlift.presentation.ui.feature.camera

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.maxlift.presentation.ui.common.IconTextButton
import com.maxlift.presentation.ui.feature.exercise.SelectTypePopUp
import com.maxlift.presentation.ui.feature.exercise.SelectWeightPopUp
import com.maxlift.presentation.ui.feature.exercise.blendColors
import com.maxlift.presentation.ui.feature.person.SelectPersonPopUp
import com.maxlift.presentation.ui.feature.person.list.PersonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private var scaleX: Float = 0f
private var scaleY: Float = 0f
private var offsetX: Float = 0f

@OptIn(ExperimentalGetImage::class)
@Composable
fun MLKitObjectDetectionScreen(viewModel: CameraViewModel, personViewModel: PersonViewModel, navController: NavController) {
    val context = LocalContext.current
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var isProcessingMovement by remember { mutableStateOf(false) }

    var timeColor by remember { mutableStateOf(Color.Green) }
    val times by viewModel.times.observeAsState(null)

    var myDetectedObjects by remember { mutableStateOf<List<Pair<RectF, Int>>>(emptyList()) }

    var selectedBoxId by remember { mutableIntStateOf(-1) }
    var cropRect by remember { mutableStateOf<Rect?>(null) }

    val previewView = remember { PreviewView(context).apply {
        this.scaleType = PreviewView.ScaleType.FILL_CENTER
        this.background = ColorDrawable(0)
    } }

    var showWeightPopUp by remember { mutableStateOf(false) }
    var showPersonPopUp by remember { mutableStateOf(false) }
    var showTypePopUp by remember { mutableStateOf(false) }

    val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableMultipleObjects()
        .build()
    val objectDetector = ObjectDetection.getClient(options)

    LaunchedEffect(Unit) {
        viewModel.resetBoundingBoxProcessing()
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
                        myDetectedObjects = detectedObjects.filter { obj ->
                            obj.boundingBox.width() / obj.boundingBox.height() > 0.5 &&
                            obj.boundingBox.width() / obj.boundingBox.height() < 1.5
                        }.map {
                            Pair(RectF(it.boundingBox), it.trackingId!!)
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

    LaunchedEffect(myDetectedObjects) {
        if(isProcessingMovement) {
            for (obj in myDetectedObjects) {
                if (obj.second == selectedBoxId) {
                    sendToBackgroundProcessing(obj.first, viewModel)
                }
            }
        }
    }

    LaunchedEffect(cropRect) {
        cropRect?.let {
            setDrawingOffsetAndScale(previewView, cropRect!!)
        }
    }

    LaunchedEffect(times?.last()) {
        if(times != null && times?.size!! > 1) {
            timeColor = generateColor(times?.last()!!, times?.get(times?.size!! - 2)!!)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxWidth().weight(1f)) {
                AndroidView( { previewView } , Modifier.fillMaxWidth())

                MultipleBoundingBoxOverlay(
                    detectedObjects = myDetectedObjects,
                    boxColor = MaterialTheme.colorScheme.primary,
                    selectedId = selectedBoxId,
                    onSelect = {
                        selectedBoxId = it
                    }
                )

                Text(
                    text = if(times?.isNotEmpty() == true)"${times?.last()} ms" else "",
                    style = MaterialTheme.typography.titleLarge,
                    color = timeColor,
                    modifier = Modifier.align(Alignment.TopCenter).padding(10.dp)
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        Spacer(Modifier.size(0.dp))
                        IconTextButton(icon = Icons.Default.FitnessCenter, "", 28.dp) {
                            if (!isProcessingMovement) showTypePopUp = true
                        }
                        IconTextButton(icon = Icons.Default.Person, "", 30.dp) {
                            if (!isProcessingMovement) showPersonPopUp = true
                        }
                        IconTextButton(icon = Icons.Default.Scale, "", 26.dp) {
                            if (!isProcessingMovement) showWeightPopUp = true
                        }
                        Spacer(Modifier.size(0.dp))
                    }

                    Box(Modifier.wrapContentSize().padding(vertical = 10.dp)) {
                        Crossfade(targetState = isProcessingMovement || myDetectedObjects.isNotEmpty()) { active ->
                            if (active) {
                                RecordButton {
                                    isProcessingMovement = !isProcessingMovement
                                    if (isProcessingMovement) {
                                        viewModel.resetBoundingBoxProcessing()
                                    } else {
                                        if (times?.isNotEmpty() == true) {
                                            if (currentDestination == "mlkit") {
                                                navController.navigate("result")
                                            }
                                        }
                                    }
                                }
                            } else {
                                DisabledRecordButton()
                            }
                        }
                    }
                }
            }

            if (showWeightPopUp) {
                SelectWeightPopUp(onDismiss = { showWeightPopUp = false }) { weight ->
                    sharedPreferences.edit().putInt("weight", weight.toInt()).apply()
                }
            }

            if (showPersonPopUp) {
                SelectPersonPopUp(personViewModel = personViewModel, onDismiss = { showPersonPopUp = false }) { person ->
                    sharedPreferences.edit().putInt("person", person.toInt()).apply()
                }
            }

            if (showTypePopUp) {
                SelectTypePopUp(onDismiss = { showTypePopUp = false }) { type ->
                    sharedPreferences.edit().putString("type", type).apply()
                }
            }
        }
    }
}

@Composable
fun MultipleBoundingBoxOverlay(
    detectedObjects: List<Pair<RectF, Int>>,
    selectedId: Int,
    onSelect: (Int) -> Unit,
    boxColor: Color
) {
    val currentBoxes by rememberUpdatedState(detectedObjects)
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(detectedObjects.map { it.second }) {
                detectTapGestures(
                    onTap = { offset ->
                        currentBoxes.forEach { obj ->
                            val box = scaleBoundingBox(obj.first)
                            val left = maxOf(box.left - offsetX, 0f)
                            val right = maxOf(box.right - offsetX, box.width())

                            val boxCenter = RectF(
                                left - 50,
                                box.centerY() - 50,
                                right + 50,
                                box.centerY() + 50
                            )

                            if (boxCenter.contains(offset.x, offset.y)) {
                                onSelect(obj.second)
                            }
                        }
                    }
                )
            }
    ) {
        for (obj in detectedObjects) {
            val box = scaleBoundingBox(obj.first)
            val left = maxOf(box.left - offsetX, 0f)
            val right = maxOf(box.right - offsetX, box.width())

            val topLeft = Offset(left, box.top)
            val bottomRight = Offset(right, box.bottom)
            val centerX = (left + right) / 2

            if (obj.second == selectedId) {
                drawRect(
                    color = boxColor,
                    topLeft = topLeft,
                    size = Size(
                        width = bottomRight.x - topLeft.x,
                        height = bottomRight.y - topLeft.y
                    ),
                    alpha = 0.35f,
                )
            } else {
                drawCircle(
                    color = boxColor,
                    radius = 40f,
                    center = Offset(centerX, box.centerY()),
                )
            }

//            drawContext.canvas.nativeCanvas.apply {
//                val paint = android.graphics.Paint().apply {
//                    color = android.graphics.Color.BLACK
//                    textAlign = android.graphics.Paint.Align.CENTER
//                    textSize = 40f
//                }
//                drawText(obj.second.toString(), centerX, centerY, paint)
//            }
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
    val realWidth = (previewView.height.toFloat() / cropRect.height()) * cropRect.width()

    offsetX = (realWidth - previewView.width) / 2
    scaleX = previewView.height.toFloat() * cameraAspectRatio / cropRect.width()
    scaleY = previewView.height.toFloat() / cropRect.height()
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