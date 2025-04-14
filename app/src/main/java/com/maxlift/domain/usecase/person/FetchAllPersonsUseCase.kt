package com.maxlift.domain.usecase.person

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Person

class FetchAllPersonsUseCase {
    companion object {
        suspend fun execute(context: Context): List<Person> {
            val database = AppDatabase.getDatabase(context)
            return MyRepository(
                 database.exerciseDataSource(),
                 database.personDataSource()
            ).fetchAllPersons()
        }
    }
}