package com.maxlift.domain.usecase.exercise

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveExerciseUseCase {
    companion object {
        suspend fun execute(context: Context, exercise: Exercise) {
            withContext(Dispatchers.IO) {
                val appDatabase = AppDatabase.getDatabase(context)
                MyRepository(
                    appDatabase.exerciseDataSource(),
                    appDatabase.personDataSource()
                ).saveExercise(exercise)
            }
        }
    }
}