package com.maxlift.domain.usecase.login

import android.util.Log
import com.maxlift.data.repository.UserRepository

class LoginUseCase(private var userRepository: UserRepository) {
    fun execute(credentials: Credentials): Boolean {
        try {
            val user = userRepository.fetchUserByEmail(credentials.email)
            println(user?.email)
            user?.let {
                return true
            }
            return false
        }catch (error: Exception) {
            Log.e("LoginUseCase", "Error in login")
            return false
        }
    }
}