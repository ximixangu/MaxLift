package com.maxlift.data.datasource.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<Float> {
        return value.split(",").map { it.toFloat() }
    }

    @TypeConverter
    fun fromList(list: List<Float>): String {
        return list.joinToString(",")
    }
}