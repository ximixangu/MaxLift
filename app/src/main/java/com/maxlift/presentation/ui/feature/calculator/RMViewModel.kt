package com.maxlift.presentation.ui.feature.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxlift.domain.usecase.rmCompute.RMParameters

class RMViewModel: ViewModel() {
    private val _rmState = MutableLiveData<Double?>()
    val rm: LiveData<Double?> get() = _rmState

    /**
     * Computes the RM with the given [rmParams] desired formula.
     * By default Epley is used with reps <= 10. Brzycki is otherwise.
     * @param rmParams has the weight, repetitions and desired formula.
     */
    fun computeRM(rmParams: RMParameters) {
        val weight = rmParams.weight.toInt()
        val reps = rmParams.repetitions.toInt()

        when(rmParams.formula) {
            "Epley" -> _rmState.value = computeEpley(weight, reps)
            "Brzycki" -> _rmState.value = computeBrzycki(weight, reps)
            "Mean" -> _rmState.value = computeMean(weight, reps)
            else -> {
                if(reps <= 10) _rmState.value = computeEpley(weight, reps)
                else _rmState.value = computeBrzycki(weight, reps)
            }
        }
    }

    private fun computeBrzycki(weight: Int, reps: Int): Double {
        return (weight / (1.0278 - (0.0278 * reps)))
    }

    private fun computeEpley(weight: Int, reps: Int): Double {
        return (weight * (1 + 0.033 * reps))
    }

    private fun computeMean(weight: Int, reps: Int): Double {
        return (computeEpley(weight, reps) + computeBrzycki(weight, reps)) / 2
    }
}