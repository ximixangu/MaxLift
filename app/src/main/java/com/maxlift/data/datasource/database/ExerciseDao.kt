package com.maxlift.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maxlift.data.model.database.ExerciseEntity
import com.maxlift.domain.model.ExerciseSummary

@Dao
interface ExerciseDao {
    @Insert
    fun save(exercise: ExerciseEntity)

    @Update
    fun update(exercise: ExerciseEntity)

    @Query("DELETE FROM exercise WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT id, personId, type, numberOfRepetitions, weight, date, description, title, times " +
            "FROM exercise WHERE id = :id")
    fun getExerciseById(id: Int): ExerciseEntity?

    @Query("SELECT id, type, weight, numberOfRepetitions, date, title FROM exercise " +
            "WHERE personId = :personId ORDER BY id DESC")
    fun getExercisesByPerson(personId: Int): List<ExerciseSummary>

    @Query("""
        SELECT id, type, weight, numberOfRepetitions, date, title FROM exercise WHERE
        (:personId IS NULL OR personId = :personId) AND
        (:title IS NULL OR title LIKE '%' || :title || '%' OR type LIKE '%' || :title || '%') AND
        (:minWeight IS NULL OR weight >= :minWeight) AND
        (:maxWeight IS NULL OR weight <= :maxWeight) AND
        (:minRepetitions IS NULL OR numberOfRepetitions >= :minRepetitions) AND
        (:maxRepetitions IS NULL OR numberOfRepetitions <= :maxRepetitions) AND
        (:startDate IS NULL OR date >= :startDate) AND
        (:endDate IS NULL OR date <= :endDate)
        ORDER BY CASE :sortField
            WHEN 'weight' THEN weight
            WHEN 'reps' THEN numberOfRepetitions
            WHEN 'type' THEN type
            ELSE id
        END DESC
    """)
    fun searchQueryExercises(
        personId: Int?,
        title: String?,
        minWeight: Float? = null,
        maxWeight: Float? = null,
        minRepetitions: Int? = null,
        maxRepetitions: Int? = null,
        startDate: String? = null,
        endDate: String? = null,
        sortField: String? = "id"
    ): List<ExerciseSummary>
}