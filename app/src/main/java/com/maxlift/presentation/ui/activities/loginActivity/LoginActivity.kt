package com.maxlift.presentation.ui.activities.loginActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.maxlift.data.datasource.UserDataSource
import com.maxlift.data.repository.UserRepository
import com.maxlift.domain.usecase.login.LoginUseCase
import com.maxlift.presentation.theme.MaxLiftTheme

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaxLiftTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserLoginForm(
                        LoginUseCase(
                            UserRepository(
                                UserDataSource.getInstance(
                                    LocalContext.current
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}