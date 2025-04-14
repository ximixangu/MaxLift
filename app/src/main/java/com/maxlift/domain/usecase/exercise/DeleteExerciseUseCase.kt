package com.maxlift.domain.usecase.exercise

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository

class DeleteExerciseUseCase {
    companion object {
        suspend fun execute(context: Context, id: Int) {
            val database = AppDatabase.getDatabase(context)
            MyRepository(
                database.exerciseDataSource(),
                database.personDataSource()
            ).deleteExerciseById(id)
        }
    }
}