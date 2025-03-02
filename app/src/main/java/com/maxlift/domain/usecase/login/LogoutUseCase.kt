package com.maxlift.domain.usecase.login

import android.util.Log
import com.maxlift.data.repository.UserRepository

class LogoutUseCase(private var userRepository: UserRepository) {
    fun execute(): Boolean {
        try {
            val user = userRepository.getLoggedUser()
            user?.let {
                userRepository.setLoggedUser(null)
                return true
            }
            return false
        }catch (error: Exception) {
            Log.e("LoginUseCase", "Error in login")
            return false
        }
    }
}