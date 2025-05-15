package com.maxlift.presentation.ui.view.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.usecase.exercise.DeleteExerciseUseCase
import com.maxlift.domain.usecase.exercise.FetchExerciseUseCase
import com.maxlift.domain.usecase.exercise.FetchExercisesByPersonUseCase
import com.maxlift.domain.usecase.exercise.FetchExercisesByPersonWithFiltersUseCase
import com.maxlift.domain.usecase.exercise.UpdateExerciseUseCase
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val fetchExercisesByPersonUseCase: FetchExercisesByPersonUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val fetchExercisesByPersonWithFiltersUseCase: FetchExercisesByPersonWithFiltersUseCase,
    private val fetchExerciseUseCase: FetchExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase,
): ViewModel() {
    private val _exercise = MutableLiveData<Exercise?>(null)
    val exercise: LiveData<Exercise?> = _exercise

    private var _exerciseList = MutableLiveData<List<Exercise>?>()
    val exerciseListState: LiveData<List<Exercise>?> = _exerciseList

    fun fetchExercise(id: Int) {
        viewModelScope.launch {
            try {
                _exercise.value = fetchExerciseUseCase(id)
            } catch (e: Exception) {
                println("Error fetching exercise: ${e.message}")
            }
        }
    }

    fun fetchExercisesByPerson(id: Int) {
        try {
            viewModelScope.launch {
                _exerciseList.value = fetchExercisesByPersonUseCase(id)
            }
        } catch (e: Exception) {
            println("Error fetching person exercises: ${e.message}")
        }
    }

    fun fetchExercisesSearch(
        id: Int,
        title: String?,
        minWeight: Int?,
        maxWeight: Int?,
        minReps: Int?,
        maxReps: Int?,
        startDate: String?,
        endDate: String?,
        sortField: String?,
    ) {
        try {
            viewModelScope.launch {
                _exerciseList.value = fetchExercisesByPersonWithFiltersUseCase(
                    id,
                    title,
                    minWeight,
                    maxWeight,
                    minReps,
                    maxReps,
                    if (startDate?.isEmpty() == true) null else startDate,
                    if (endDate?.isEmpty() == true) null else endDate,
                    sortField
                )
            }
        } catch (e: Exception) {
            println("Error fetching exercises with filters: ${e.message}")
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            try {
                updateExerciseUseCase(exercise)
                _exercise.value = exercise
            } catch (e: Exception) {
                println("Error updating exercise: ${e.message}")
            }
        }
    }

    fun deleteExercise(id: Int) {
        viewModelScope.launch {
            try {
                deleteExerciseUseCase(id)
            } catch (e: Exception) {
                println("Error fetching exercise: ${e.message}")
            }
        }
    }
}