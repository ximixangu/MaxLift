package com.maxlift.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.maxlift.domain.model.Exercise
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "personId") val personId: Int,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "times") val times: List<Float>,
    @ColumnInfo(name = "numberOfReps") val numberOfRepetitions: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
){
    companion object {
        private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        fun fromExerciseDomain(exercise: Exercise): ExerciseEntity {
            return ExerciseEntity(
                exercise.id,
                exercise.personId,
                exercise.type,
                exercise.weight,
                exercise.times,
                exercise.numberOfRepetitions,
                formatter.format(exercise.date),
                exercise.title,
                exercise.description
            )
        }
    }
}

fun ExerciseEntity.toExerciseDomain(): Exercise {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
    return Exercise(
        id,
        personId,
        type,
        weight,
        times,
        numberOfRepetitions,
        formatter.parse(date)!!,
        title,
        description
    )
}