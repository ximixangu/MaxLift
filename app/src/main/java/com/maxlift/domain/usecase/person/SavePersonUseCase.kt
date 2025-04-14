package com.maxlift.domain.usecase.person

import android.content.Context
import com.maxlift.data.datasource.database.AppDatabase
import com.maxlift.data.repository.MyRepository
import com.maxlift.domain.model.Person

class SavePersonUseCase{
    companion object {
        suspend fun execute(context: Context, person: Person) {
            val database = AppDatabase.getDatabase(context)
            MyRepository(
                database.exerciseDataSource(),
                database.personDataSource()
            ).savePerson(person)
        }
    }
}