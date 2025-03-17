package com.maxlift.domain.model

import java.util.Date
import java.util.UUID

class Measurement(
    val uuid: UUID,
    val repsTimes: List<Float>,
    val totalReps: Int,
    val date: Date,
) {}