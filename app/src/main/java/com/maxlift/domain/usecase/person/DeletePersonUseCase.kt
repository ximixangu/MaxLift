package com.maxlift.domain.usecase.person

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeletePersonUseCase {
    companion object {
        suspend fun execute(context: Context, id: Int) {
            withContext(Dispatchers.IO) {
                val appDatabase = AppDatabase.getDatabase(context)
                MyRepository(
                    appDatabase.exerciseDataSource(),
                    appDatabase.personDataSource()
                ).deletePersonById(id)
            }
        }
    }
}