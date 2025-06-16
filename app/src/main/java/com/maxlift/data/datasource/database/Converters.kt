package com.maxlift.data.datasource.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
    @TypeConverter
    fun fromString(value: String): List<Float> {
        return value.split(",").map { it.toFloat() }
    }
    @TypeConverter
    fun fromList(list: List<Float>): String {
        return list.joinToString(",")
    }
    @TypeConverter
    fun fromDate(date: Date): String {
        return formatter.format(date)
    }
    @TypeConverter
    fun fromTimestamp(value: String): Date? {
        return formatter.parse(value)
    }
}