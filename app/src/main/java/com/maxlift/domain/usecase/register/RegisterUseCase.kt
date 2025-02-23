package com.maxlift.domain.usecase.register

import android.util.Log
import com.maxlift.data.repository.UserRepository
import com.maxlift.domain.model.User
import java.util.UUID

class RegisterUseCase(private var userRepository: UserRepository) {
     fun execute(registerCredentials: RegisterCredentials): Boolean {
        try {
            val existingUser = userRepository.fetchUserByEmail(email = registerCredentials.email)
            existingUser?.let {
                return false
            }

            var uuid: UUID? = null
            while(uuid == null) {
                uuid = UUID.randomUUID()
                if(userRepository.fetchUserByUUID(uuid) != null) {
                    uuid = null
                }
            }

            userRepository.saveUser(
                User(
                    name = registerCredentials.name,
                    uuid = uuid,
                    email = registerCredentials.email,
                )
            )

            return true
        }catch (error: Exception) {
            Log.e("RegisterUseCase", "Error in registration")
            return false
        }
    }
}