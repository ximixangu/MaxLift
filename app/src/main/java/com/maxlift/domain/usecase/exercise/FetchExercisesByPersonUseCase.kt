package com.maxlift.domain.usecase.exercise

import com.maxlift.domain.model.Exercise
import com.maxlift.domain.repository.IMyRepository

class FetchExercisesByPersonUseCase(
    private val myRepository: IMyRepository
){
    suspend operator fun invoke(id: Int): List<Exercise> {
        return myRepository.fetchExercisesByPersonId(id)
    }
}