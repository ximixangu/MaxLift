package com.maxlift.domain.usecase.person

import com.maxlift.domain.repository.IMyRepository

class DeletePersonUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(id: Int) {
        myRepository.deletePersonById(id)
    }
}