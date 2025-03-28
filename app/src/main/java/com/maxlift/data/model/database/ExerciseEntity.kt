package com.maxlift.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "description") val description: String?,
)