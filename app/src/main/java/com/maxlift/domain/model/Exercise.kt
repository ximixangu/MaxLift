package com.maxlift.domain.model

import java.util.Date

class Exercise(
    val id: Int,
    val personId: Int,
    val type: String,
    val weight: Float,
    val times: List<Float>,
    val numberOfRepetitions: Int,
    val date: Date,
    val description: String?,
) {}