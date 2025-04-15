package com.maxlift.domain.usecase.exercise

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Exercise

class FetchExercisesByPersonWithFiltersUseCase {
    companion object {
        suspend fun execute(
            context: Context,
            id: Int,
            title: String?,
            minWeight: Int?,
            maxWeight: Int?,
            minReps: Int?,
            maxReps: Int?,
        ): List<Exercise> {
            val database = AppDatabase.getDatabase(context)
            return MyRepository(
                database.exerciseDataSource(),
                database.personDataSource()
            ).fetchExercisesByPersonWithFilters(
                id,
                title,
                minWeight,
                maxWeight,
                minReps,
                maxReps
            )
        }
    }
}