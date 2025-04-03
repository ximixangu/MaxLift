package com.maxlift.domain.usecase.exercise

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchExercisesByPersonUseCase {
    companion object {
        suspend fun execute(context: Context, id: Int): List<Exercise> {
            return withContext(Dispatchers.IO) {
                val myAppDatabase = AppDatabase.getDatabase(context)
                MyRepository(
                    myAppDatabase.exerciseDataSource(),
                    myAppDatabase.personDataSource()
                ).fetchExercisesByPersonId(id)
            }
        }
    }
}