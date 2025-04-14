package com.maxlift.domain.usecase.exercise

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Exercise

class FetchExercisesByPersonUseCase {
    companion object {
        suspend fun execute(context: Context, id: Int): List<Exercise> {
            val database = AppDatabase.getDatabase(context)
            return MyRepository(
                database.exerciseDataSource(),
                database.personDataSource()
            ).fetchExercisesByPersonId(id)
        }
    }
}