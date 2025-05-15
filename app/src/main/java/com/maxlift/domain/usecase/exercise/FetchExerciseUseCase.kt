package com.maxlift.domain.usecase.exercise

import com.maxlift.domain.model.Exercise
import com.maxlift.domain.repository.IMyRepository

class FetchExerciseUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(id: Int): Exercise? {
        return myRepository.fetchExerciseById(id)
    }
}