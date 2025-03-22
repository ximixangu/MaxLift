package com.maxlift.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maxlift.data.datasource.UserDataSource
import com.maxlift.data.repository.UserRepository
import com.maxlift.domain.usecase.GetLoggedUserUseCase
import com.maxlift.domain.usecase.login.LoginUseCase
import com.maxlift.domain.usecase.login.LogoutUseCase
import com.maxlift.domain.usecase.register.RegisterUseCase
import com.maxlift.presentation.ui.common.MyScaffoldTopAppBar
import com.maxlift.presentation.ui.feature.calculator.RMForm
import com.maxlift.presentation.ui.feature.calculator.RMViewModel
import com.maxlift.presentation.ui.feature.calculator.ResultScreen
import com.maxlift.presentation.ui.feature.camera.CameraViewModel
import com.maxlift.presentation.ui.feature.camera.MLKitObjectDetectionScreen
import com.maxlift.presentation.ui.feature.camera.TFLiteObjectDetectionScreen
import com.maxlift.presentation.ui.feature.menu.MenuScreen
import com.maxlift.presentation.ui.feature.user.ProfileScreen
import com.maxlift.presentation.ui.feature.user.UserLoginForm
import com.maxlift.presentation.ui.feature.user.UserRegisterForm
import com.maxlift.presentation.ui.feature.user.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val userRepository = UserRepository(UserDataSource.getInstance(LocalContext.current))
    val navController = rememberNavController()
    val sharedViewModel: CameraViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current ?:
        error("No ViewModelStoreOwner found")
    )

    MyScaffoldTopAppBar(navController, LogoutUseCase(userRepository)) { innerPadding ->
        NavHost(navController = navController, startDestination = "menu", Modifier.padding(innerPadding)) {
            composable("menu") { MenuScreen(navController) }
            composable("login") { UserLoginForm(LoginUseCase(userRepository), navController) }
            composable("register") { UserRegisterForm(RegisterUseCase(userRepository), navController) }
            composable("camera") { TFLiteObjectDetectionScreen() }
            composable("profile") { ProfileScreen(UserViewModel(GetLoggedUserUseCase(userRepository))) }
            composable("calculator") { RMForm(RMViewModel(), navController) }
            composable("result") { ResultScreen(sharedViewModel) }
            composable("mlkit") { MLKitObjectDetectionScreen(sharedViewModel) }
        }
    }
}

