package com.maxlift.domain.usecase.exercise

import com.maxlift.domain.model.Exercise
import com.maxlift.domain.repository.IMyRepository

class FetchExercisesByPersonWithFiltersUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(
        id: Int,
        title: String?,
        minWeight: Int?,
        maxWeight: Int?,
        minReps: Int?,
        maxReps: Int?,
        startDate: String?,
        endDate: String?,
        sortField: String?,
    ): List<Exercise>? {
        return myRepository.fetchExercisesByPersonWithFilters(
            id,
            title,
            minWeight,
            maxWeight,
            minReps,
            maxReps,
            startDate,
            endDate,
            sortField,
        )
    }
}