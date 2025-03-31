package com.maxlift.domain.repository

import com.maxlift.domain.model.Exercise
import com.maxlift.domain.model.Person

interface IMyRepository {
    fun fetchExerciseById(id: Int): Exercise?
    fun fetchExercisesByPersonId(personId: Int): List<Exercise>?
    fun fetchPersonById(personId: Int): Person?
    fun fetchAllPersons(): List<Person>
    fun savePerson(person: Person)
    fun saveExercise(exercise: Exercise)
    fun updatePerson(person: Person)
    fun updateExercise(exercise: Exercise)
    fun deleteExerciseById(id: Int)
    fun deletePersonById(id: Int)
}