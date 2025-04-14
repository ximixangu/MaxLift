package com.maxlift.domain.usecase.person

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository

class DeletePersonUseCase {
    companion object {
        suspend fun execute(context: Context, id: Int) {
            val database = AppDatabase.getDatabase(context)
            MyRepository(
                database.exerciseDataSource(),
                database.personDataSource()
            ).deletePersonById(id)
        }
    }
}