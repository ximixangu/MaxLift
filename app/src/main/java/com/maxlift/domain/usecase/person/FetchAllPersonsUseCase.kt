package com.maxlift.domain.usecase.person

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchAllPersonsUseCase {
    companion object {
        suspend fun execute(context: Context): List<Person> {
            return withContext(Dispatchers.IO){
                val database = AppDatabase.getDatabase(context)
                 MyRepository(
                     database.exerciseDataSource(),
                     database.personDataSource()
                ).fetchAllPersons()
            }
        }
    }
}