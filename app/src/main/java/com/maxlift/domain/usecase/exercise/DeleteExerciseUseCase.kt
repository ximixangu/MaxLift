package com.maxlift.domain.usecase.exercise

import com.maxlift.domain.repository.IMyRepository

class DeleteExerciseUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(id: Int) {
        myRepository.deleteExerciseById(id)
    }
}