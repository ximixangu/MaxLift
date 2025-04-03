package com.maxlift.domain.usecase.person

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SavePersonUseCase {
    companion object {
        suspend fun execute(context: Context, person: Person) {
            withContext(Dispatchers.IO) {
                val appDatabase = AppDatabase.getDatabase(context)
                val myRepository = MyRepository(appDatabase.exerciseDataSource(), appDatabase.personDataSource())
                myRepository.savePerson(person)
            }
        }
    }
}