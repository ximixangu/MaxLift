package com.maxlift.domain.model

import java.util.Date

data class ExerciseSummary(
    val id: Int,
    var type: String,
    var weight: Float,
    var numberOfRepetitions: Int,
    val date: Date,
    var title: String?,
)