package com.maxlift.presentation.ui.activities.cameraActivity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.maxlift.presentation.theme.MaxLiftTheme
import com.maxlift.presentation.ui.common.MyScaffold

class CameraActivity : ComponentActivity() {
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) {
                setCameraPreview()
            } else {
                // NO Permission
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {
                setCameraPreview()
            } else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun setCameraPreview() {
        enableEdgeToEdge()
        setContent {
             MaxLiftTheme {
                 MyScaffold { innerPadding ->
                     Surface(
                         modifier = Modifier
                             .fillMaxSize()
                             .padding(innerPadding),
                         color = MaterialTheme.colorScheme.background
                     ) {
                         CameraPreviewScreen()
                     }
                }
            }
        }
    }
}

