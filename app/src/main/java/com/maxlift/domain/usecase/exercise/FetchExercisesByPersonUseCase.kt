package com.maxlift.domain.usecase.exercise

import com.maxlift.domain.model.ExerciseSummary
import com.maxlift.domain.repository.IMyRepository

class FetchExercisesByPersonUseCase(
    private val myRepository: IMyRepository
){
    suspend operator fun invoke(id: Int): List<ExerciseSummary> {
        return myRepository.fetchExercisesByPersonId(id)
    }
}