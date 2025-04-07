package com.maxlift.presentation.ui.feature.exercise

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.usecase.exercise.DeleteExerciseUseCase
import com.maxlift.domain.usecase.exercise.FetchExerciseUseCase
import kotlinx.coroutines.launch

class ExerciseViewModel: ViewModel() {
    private val _exercise = MutableLiveData<Exercise?>(null)
    val exercise: LiveData<Exercise?> = _exercise

    fun fetchExercise(context: Context, id: Int) {
        viewModelScope.launch {
            try {
                _exercise.value = FetchExerciseUseCase.execute(context, id)
            } catch (e: Exception) {
                println("Error fetching exercise: ${e.message}")
            }
        }
    }

    fun deleteExercise(context: Context, id: Int) {
        viewModelScope.launch {
            try {
                DeleteExerciseUseCase.execute(context, id)
            } catch (e: Exception) {
                println("Error fetching exercise: ${e.message}")
            }
        }
    }
}