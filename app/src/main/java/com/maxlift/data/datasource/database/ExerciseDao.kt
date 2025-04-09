package com.maxlift.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maxlift.data.model.database.ExerciseEntity

@Dao
interface ExerciseDao {
    @Insert
    fun save(exercise: ExerciseEntity)

    @Update
    fun update(exercise: ExerciseEntity)

    @Query("DELETE FROM exercise WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM exercise WHERE id = :id")
    fun getExerciseById(id: Int): ExerciseEntity?

    @Query("SELECT * FROM exercise WHERE personId = :personId ORDER BY id DESC")
    fun getExercisesByPerson(personId: Int): List<ExerciseEntity>

    @Query(
        "SELECT * FROM exercise WHERE personId = :personId " +
        "AND (title LIKE '%' || :title || '%' OR type LIKE '%' || :title || '%') " +
        "ORDER BY id DESC"
    )
    fun getExercisesByPersonAndTitle(personId: Int, title: String): List<ExerciseEntity>

    @Query("SELECT * FROM exercise ORDER BY date DESC")
    fun getAllExercises(): List<ExerciseEntity>
}