package com.maxlift.domain.usecase.exercise

import com.maxlift.domain.model.Exercise
import com.maxlift.domain.repository.IMyRepository

class UpdateExerciseUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(exercise: Exercise) {
        myRepository.updateExercise(exercise)
    }
}