package com.maxlift.data.datasource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maxlift.data.model.database.ExerciseEntity

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: ExerciseEntity)

    @Update
    suspend fun update(exercise: ExerciseEntity)

    @Delete
    suspend fun delete(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercise WHERE personId = :personId ORDER BY id DESC")
    fun getExercisesByPerson(personId: Int): List<ExerciseEntity>

    @Query("SELECT * FROM exercise ORDER BY date DESC")
    fun getAllExercises(): List<ExerciseEntity>
}