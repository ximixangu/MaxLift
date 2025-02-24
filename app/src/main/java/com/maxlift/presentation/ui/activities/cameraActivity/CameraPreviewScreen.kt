package com.maxlift.presentation.ui.activities.cameraActivity

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.maxlift.presentation.ui.common.BackButton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

enum class CameraAction{
    CAPTURE_IMAGE,
    CAPTURE_VIDEO
}

var recording: Recording? = null

@Composable
fun CameraPreviewScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lensFacing = remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }

    val isRecording = remember { mutableStateOf(false) }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val videoCapture = remember { VideoCapture.withOutput(Recorder.Builder().build()) }
    val currentAction = remember { mutableStateOf(CameraAction.CAPTURE_IMAGE) }
    val currentIcon = remember { mutableStateOf(Icons.Filled.CameraAlt) }

    LaunchedEffect(lensFacing.intValue, currentAction.value) {
        val cameraProvider = context.getCameraProvider()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing.intValue).build()
        cameraProvider.unbindAll()

        when(currentAction.value){
            CameraAction.CAPTURE_IMAGE -> {
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider },
                    imageCapture
                )
            }
            CameraAction.CAPTURE_VIDEO -> {
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider },
                    videoCapture
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f),
            contentAlignment = Alignment.BottomCenter
        ) {
            AndroidView( { previewView }, modifier = Modifier.fillMaxSize())

            BackButton(
                Modifier.align(Alignment.TopStart)
                    .padding(vertical = 32.dp, horizontal = 8.dp)
            )

            Button(
                modifier = Modifier.padding(10.dp).width(100.dp).height(100.dp),
                shape = CircleShape,
                colors =
                    if(isRecording.value) ButtonDefaults.buttonColors(Color.Red)
                    else ButtonDefaults.buttonColors(Color.Unspecified),
                onClick = {
                    when (currentAction.value) {
                        CameraAction.CAPTURE_IMAGE -> captureImage(imageCapture, context)
                        CameraAction.CAPTURE_VIDEO -> {
                            captureVideo(videoCapture, context)
                            isRecording.value = (recording != null)
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = currentIcon.value,
                    contentDescription = "Take Photo",
                    modifier = Modifier.fillMaxSize(0.9f)
                )
            }

            Button(
                modifier = Modifier.align(Alignment.BottomStart).padding(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                shape = CircleShape,
                onClick = {
                    if (currentAction.value == CameraAction.CAPTURE_IMAGE ) {
                        currentAction.value = CameraAction.CAPTURE_VIDEO
                        currentIcon.value = Icons.Filled.Videocam
                    } else if(recording == null){
                        currentAction.value = CameraAction.CAPTURE_IMAGE
                        currentIcon.value = Icons.Filled.CameraAlt
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.SwitchCamera,
                    contentDescription = "Switch Action",
                    modifier = Modifier.size(40.dp)
                )
            }


            Button(
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                shape = CircleShape,
                modifier = Modifier.align(Alignment.BottomEnd).padding(vertical = 10.dp),
                onClick = {
                    lensFacing.intValue =
                        if (lensFacing.intValue == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch Lens",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // Afegir altres funcionalitats aquí (part inferior)
        }
    }
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun captureImage(imageCapture: ImageCapture, context: Context) {
    val name = "Image_${System.currentTimeMillis()}.jpeg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MaxLift")
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println("Success")
            }

            override fun onError(exception: ImageCaptureException) {
                println("Failed $exception")
            }

        })
}

private fun captureVideo(videoCapture: VideoCapture<Recorder>, context: Context) {
    if(recording != null) {
        recording?.stop()
        recording = null
        return
    }

    val name = "Video_${System.currentTimeMillis()}.mp4"
    val contentValues = ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, name)
        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
    }

    val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
        context.contentResolver,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    ).setContentValues(contentValues).build()

    recording = videoCapture.output.prepareRecording(context, mediaStoreOutputOptions)
    .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
        when (recordEvent) {
            is VideoRecordEvent.Start -> {
                println("Recording Started")
            }
            is VideoRecordEvent.Finalize -> {
                if (!recordEvent.hasError()) {
                    println("Recording Ended: ${recordEvent.outputResults.outputUri}")
                } else {
                    println("Error recording: ${recordEvent.error}")
                }
            }
        }
    }
}
