package com.maxlift.domain.usecase.rmCompute

import android.util.Log

data class RMParameters(
    var formula: String = "",
    var weight: String = "",
    var repetitions: String = ""
){
    fun isValid(): Boolean {
        return isNotEmpty() && valueIsValidInt(weight) && valueIsValidInt(repetitions)
    }

    private fun valueIsValidInt(value: String): Boolean {
        return try {
            value.toInt() > 0
        }catch (error: Exception) {
            Log.e("RMParams", "Error parsing Integer value")
            false
        }
    }

    private fun isNotEmpty(): Boolean {
        return weight.isNotEmpty() && repetitions.isNotEmpty()
    }
}
