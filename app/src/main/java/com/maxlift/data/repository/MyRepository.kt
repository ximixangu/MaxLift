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

class MyRepository(private val exerciseDao: ExerciseDao, private val personDao: PersonDao): IMyRepository {
    override fun fetchExerciseById(id: Int): Exercise? {
        val exercise = exerciseDao.getExerciseById(id)
        return exercise?.toExerciseDomain()
    }

    override fun fetchExercisesByPersonId(personId: Int): List<Exercise> {
        val exerciseList = exerciseDao.getExercisesByPerson(personId).map { entity ->
            entity.toExerciseDomain()
        }
        return exerciseList
    }

    override fun fetchPersonById(personId: Int): Person? {
        val person = personDao.getPersonById(personId)
        return person?.toPersonDomain()
    }

    override fun fetchAllPersons(): List<Person> {
        val personList = personDao.getAll()

        return personList.map { personEntity ->
            personEntity.toPersonDomain()
        }
    }

    override fun savePerson(person: Person) {
        personDao.save(PersonEntity.fromPersonDomain(person))
    }

    override fun saveExercise(exercise: Exercise) {
        exerciseDao.save(ExerciseEntity.fromExerciseDomain(exercise))
        println("Saved $exercise")
    }

    override fun updatePerson(person: Person) {
        personDao.update(PersonEntity.fromPersonDomain(person))
    }

    override fun updateExercise(exercise: Exercise) {
        exerciseDao.update(ExerciseEntity.fromExerciseDomain(exercise))
    }

    override fun deleteExerciseById(id: Int) {
        exerciseDao.delete(id)
    }

    override fun deletePersonById(id: Int) {
        personDao.delete(id)
    }
}