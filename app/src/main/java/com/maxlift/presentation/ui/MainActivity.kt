package com.maxlift.presentation.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maxlift.presentation.ui.common.MyScaffold
import com.maxlift.presentation.ui.feature.camera.CameraViewModel
import com.maxlift.presentation.ui.feature.camera.MLKitObjectDetectionScreen
import com.maxlift.presentation.ui.feature.exercise.ExerciseEditScreen
import com.maxlift.presentation.ui.feature.exercise.ExerciseViewModel
import com.maxlift.presentation.ui.feature.exercise.info.ExerciseScreen
import com.maxlift.presentation.ui.feature.person.info.PersonInfoScreen
import com.maxlift.presentation.ui.feature.person.info.PersonInfoViewModel
import com.maxlift.presentation.ui.feature.person.list.PersonListScreen
import com.maxlift.presentation.ui.feature.person.list.PersonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
        if(permissionDenied(this)) {
            doPermissionRequest(this, packageName)
        }
    }

    override fun onResume() {
        super.onResume()
        if(permissionDenied(this)) {
            doPermissionRequest(this, packageName)
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    val sharedViewModel: CameraViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current ?:
        error("No ViewModelStoreOwner found")
    )
    val personViewModel: PersonViewModel = viewModel()
    val personInfoViewModel: PersonInfoViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    MyScaffold(navController) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "persons",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("persons") { PersonListScreen(personViewModel, navController) }
            composable(
                route = "result",
                enterTransition = { slideInVertically { it } },
                exitTransition = { slideOutVertically { -it } },
                popEnterTransition = { slideInVertically { -it } },
                popExitTransition = { slideOutVertically { it } },
            ) {
                ExerciseEditScreen(sharedViewModel, personViewModel, navController)
            }
            composable(
                route = "mlkit",
                enterTransition = { slideInVertically { it } },
                exitTransition = { slideOutVertically { -it } },
                popEnterTransition = { slideInVertically { -it } },
                popExitTransition = { slideOutVertically { it } },
            ) {
                MLKitObjectDetectionScreen(sharedViewModel, personViewModel, navController)
            }
            composable(
                route = "personInfo/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType }),
                enterTransition = { slideInHorizontally { it } },
                exitTransition = { slideOutHorizontally { -it } },
                popEnterTransition = { slideInHorizontally { -it } },
                popExitTransition = { slideOutHorizontally { it } },
            ) { entry ->
                PersonInfoScreen(personId = entry.arguments?.getInt("id") ?: 0, personInfoViewModel, navController)
            }
            composable(
                route = "exerciseInfo/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType }),
                enterTransition = { slideInHorizontally { it } },
                exitTransition = { slideOutHorizontally { -it } },
                popEnterTransition = { slideInHorizontally { -it } },
                popExitTransition = { slideOutHorizontally { it } },
            ) { entry ->
                ExerciseScreen(id = entry.arguments?.getInt("id") ?: 0, exerciseViewModel, navController)
            }
        }
    }
}

fun permissionDenied(context: Context): Boolean {
    return (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_DENIED)
}

fun doPermissionRequest(activity: Activity, packageName: String) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            activity, "Manifest.permission.CAMERA"))
    {
        ActivityCompat.requestPermissions(
            activity, arrayOf("Manifest.permissions.CAMERA"), 0
        )
    } else {
        AlertDialog.Builder(activity)
            .setTitle("Camera Permission Required")
            .setMessage("This app needs access to the camera. Please grant permission.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                activity.startActivity(intent)
            }
            .create()
            .show()
    }
}

