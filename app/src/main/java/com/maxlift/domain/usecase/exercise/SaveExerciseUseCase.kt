package com.maxlift.domain.usecase.exercise

import com.maxlift.domain.model.Exercise
import com.maxlift.domain.repository.IMyRepository

class SaveExerciseUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(exercise: Exercise) {
        myRepository.saveExercise(exercise)
    }
}