package com.maxlift.domain.usecase.login

import com.maxlift.data.repository.UserRepository
import com.maxlift.domain.model.User

class GetLoggedUserUseCase(private var userRepository: UserRepository) {
    fun execute(): User? {
        return userRepository.getLoggedUser()
    }
}