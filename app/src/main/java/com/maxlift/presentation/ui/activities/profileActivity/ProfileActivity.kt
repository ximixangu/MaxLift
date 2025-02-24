package com.maxlift.presentation.ui.activities.profileActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.maxlift.data.datasource.UserDataSource
import com.maxlift.data.repository.UserRepository
import com.maxlift.domain.usecase.GetLoggedUserUseCase

class ProfileActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileScreen(
                UserViewModel(
                    GetLoggedUserUseCase(
                        UserRepository(
                            UserDataSource.getInstance(
                                LocalContext.current)))))
        }
    }
}