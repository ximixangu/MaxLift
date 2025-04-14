package com.maxlift.domain.usecase.exercise

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Exercise

class UpdateExerciseUseCase {
    companion object {
        suspend fun execute(context: Context, exercise: Exercise) {
            val appDatabase = AppDatabase.getDatabase(context)
            MyRepository(
                appDatabase.exerciseDataSource(),
                appDatabase.personDataSource()
            ).updateExercise(exercise)
        }
    }
}