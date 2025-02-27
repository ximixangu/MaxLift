package com.maxlift.domain.usecase.rmCompute

import android.util.Log

data class RMParameters(
    var gender: String = "",
    var weight: String = "",
    var repetitions: String = ""
){
    fun isValid(): Boolean {
        return isNotEmpty() && genderIsValid() && valueIsValidInt(weight) && valueIsValidInt(repetitions)
    }

    private fun valueIsValidInt(value: String): Boolean {
        return try {
            value.toInt() > 0
        }catch (error: Exception) {
            Log.e("RMParams", "Error parsing Integer value")
            false
        }
    }

    private fun genderIsValid(): Boolean {
        return gender.lowercase() in listOf("male", "female", "other", "m", "f", "o")
    }

    private fun isNotEmpty(): Boolean {
        return gender.isNotEmpty() && weight.isNotEmpty() && repetitions.isNotEmpty()
    }
}
