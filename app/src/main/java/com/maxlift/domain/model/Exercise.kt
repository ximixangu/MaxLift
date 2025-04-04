package com.maxlift.domain.model

import java.time.Instant
import java.util.Date

class Exercise(
    val id: Int,
    var personId: Int,
    var type: String,
    var weight: Float,
    var times: List<Float>,
    var numberOfRepetitions: Int,
    val date: Date,
    var title: String?,
    var description: String?,
) {
    constructor() : this(
        -1,
        -1,
        "Generic Exercise",
        50f,
        emptyList(),
        0,
        Date.from(Instant.now()),
        null,
        null
    )
}