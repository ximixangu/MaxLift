package com.maxlift.data.repository

import com.maxlift.data.datasource.database.ExerciseDao
import com.maxlift.data.datasource.database.PersonDao
import com.maxlift.data.model.database.ExerciseEntity
import com.maxlift.data.model.database.PersonEntity
import com.maxlift.data.model.database.toExerciseDomain
import com.maxlift.data.model.database.toPersonDomain
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.model.Person
import com.maxlift.domain.repository.IMyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyRepository(private val exerciseDao: ExerciseDao, private val personDao: PersonDao): IMyRepository {
    override suspend fun fetchExerciseById(id: Int): Exercise? {
        return withContext(Dispatchers.IO) {
            exerciseDao.getExerciseById(id)?.toExerciseDomain()
        }
    }

    override suspend fun fetchExercisesByPersonId(personId: Int): List<Exercise> {
        return withContext(Dispatchers.IO) {
            exerciseDao.getExercisesByPerson(personId).map {
                it.toExerciseDomain()
            }
        }
    }

    override suspend fun fetchExercisesByPersonWithFilters(
        personId: Int,
        title: String?,
        minWeight: Int?,
        maxWeight: Int?,
        minReps: Int?,
        maxReps: Int?
    ): List<Exercise> {
        return withContext(Dispatchers.IO) {
            exerciseDao.searchQueryExercises(
                personId,
                title,
                minWeight?.toFloat(),
                maxWeight?.toFloat(),
                minReps,
                maxReps
            ).map { it.toExerciseDomain() }
        }
    }

    override suspend fun fetchPersonById(personId: Int): Person? {
        return withContext(Dispatchers.IO) {
            personDao.getPersonById(personId)?.toPersonDomain()
        }
    }

    override suspend fun fetchAllPersons(): List<Person> {
        return withContext(Dispatchers.IO) {
            personDao.getAll().map {
                it.toPersonDomain()
            }
        }
    }

    override suspend fun savePerson(person: Person) {
        withContext(Dispatchers.IO) {
            personDao.save(PersonEntity.fromPersonDomain(person))
        }
    }

    override suspend fun saveExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            exerciseDao.save(ExerciseEntity.fromExerciseDomain(exercise))
        }
    }

    override suspend fun updatePerson(person: Person) {
        withContext(Dispatchers.IO) {
            personDao.update(PersonEntity.fromPersonDomain(person))
        }
    }

    override suspend fun updateExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            exerciseDao.update(ExerciseEntity.fromExerciseDomain(exercise))
        }
    }

    override suspend fun deleteExerciseById(id: Int) {
        withContext(Dispatchers.IO) {
            exerciseDao.delete(id)
        }
    }

    override suspend fun deletePersonById(id: Int) {
        withContext(Dispatchers.IO) {
            personDao.delete(id)
        }
    }
}