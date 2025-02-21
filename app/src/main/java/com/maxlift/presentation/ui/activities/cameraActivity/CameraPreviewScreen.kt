package com.maxlift.presentation.ui.activities.cameraActivity

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.maxlift.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

enum class CameraAction{
    CAPTURE_IMAGE,
    CAPTURE_VIDEO
}

@Composable
fun CameraPreviewScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lensFacing = remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val currentAction = remember { mutableStateOf(CameraAction.CAPTURE_IMAGE) }

    LaunchedEffect(lensFacing.intValue) {
        val cameraProvider = context.getCameraProvider()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing.intValue).build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider },
            imageCapture
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
            contentAlignment = Alignment.BottomCenter
        ) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

            Button(
                modifier = Modifier.padding(20.dp).width(100.dp).height(100.dp),
                shape = CircleShape,
                onClick = {
                    when (currentAction.value) {
                        CameraAction.CAPTURE_IMAGE -> captureImage(imageCapture, context, "1")
                        CameraAction.CAPTURE_VIDEO -> captureImage2(imageCapture, context, "2")
                    }
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.photo_camera),
                    contentDescription = "Take Photo",
                    modifier = Modifier.fillMaxSize(0.9f)
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                shape = CircleShape,
                onClick = {
                    lensFacing.intValue =
                        if (lensFacing.intValue == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                },
                modifier = Modifier.align(Alignment.BottomEnd).padding(vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Switch Camera",
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
            // Afegir altres funcionalitats aquí
            Button(
                onClick = {
                    currentAction.value =
                        if (currentAction.value == CameraAction.CAPTURE_IMAGE ) {
                            CameraAction.CAPTURE_VIDEO
                        } else {
                            CameraAction.CAPTURE_IMAGE
                        }
                    println(currentAction.value)
                }
            ) {
                Text("Swap Func")
            }
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

private fun captureImage(imageCapture: ImageCapture, context: Context, text: String) {
    val name = text + "Image_${System.currentTimeMillis()}.jpeg"
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
                println("Success"+text)
            }

            override fun onError(exception: ImageCaptureException) {
                println("Failed $exception")
            }

        })
}

private fun captureImage2(imageCapture: ImageCapture, context: Context, text: String) {
    val name = text + "Image_${System.currentTimeMillis()}.jpeg"
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
                println("Success"+text)
            }

            override fun onError(exception: ImageCaptureException) {
                println("Failed $exception")
            }

        })
}