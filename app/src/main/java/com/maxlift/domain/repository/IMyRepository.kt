package com.maxlift.domain.repository

import com.maxlift.domain.model.Exercise
import com.maxlift.domain.model.Person

interface IMyRepository {
    suspend fun fetchExerciseById(id: Int): Exercise?
    suspend fun fetchExercisesByPersonId(personId: Int): List<Exercise>?
    suspend fun fetchExercisesByPersonWithFilters(
        personId: Int,
        title: String?,
        minWeight: Int?,
        maxWeight: Int?,
        minReps: Int?,
        maxReps: Int?
    ): List<Exercise>?
    suspend fun fetchPersonById(personId: Int): Person?
    suspend fun fetchAllPersons(): List<Person>
    suspend fun savePerson(person: Person)
    suspend fun saveExercise(exercise: Exercise)
    suspend fun updatePerson(person: Person)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun deleteExerciseById(id: Int)
    suspend fun deletePersonById(id: Int)
}