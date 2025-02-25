package com.maxlift.presentation.ui.activities.loginActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.maxlift.data.datasource.UserDataSource
import com.maxlift.data.repository.UserRepository
import com.maxlift.domain.usecase.login.LoginUseCase
import com.maxlift.presentation.theme.MaxLiftTheme
import com.maxlift.presentation.ui.common.MyScaffold

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        UserLoginForm(
                            LoginUseCase(
                                UserRepository(
                                    UserDataSource.getInstance(
                                        LocalContext.current))))
                    }
                }
            }
        }
    }
}