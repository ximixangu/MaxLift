package com.maxlift.presentation.ui.feature.person.info

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.model.Person
import com.maxlift.domain.usecase.exercise.FetchExercisesByPersonUseCase
import com.maxlift.domain.usecase.exercise.FetchExercisesByPersonWithFiltersUseCase
import com.maxlift.domain.usecase.person.DeletePersonUseCase
import com.maxlift.domain.usecase.person.EditPersonUseCase
import com.maxlift.domain.usecase.person.FetchPersonUseCase
import kotlinx.coroutines.launch

class PersonInfoViewModel: ViewModel() {
    private var _person = MutableLiveData<Person>()
    val personState: LiveData<Person> = _person

    private var _exerciseList = MutableLiveData<List<Exercise>>()
    val exerciseListState: LiveData<List<Exercise>> = _exerciseList

    fun fetchPersonAndExercises(context: Context, id: Int) {
        viewModelScope.launch {
            fetchPerson(context, id)
            fetchExercisesByPerson(context)
        }
    }

    private suspend fun fetchPerson(context: Context, id: Int) {
        try {
            _person.value = FetchPersonUseCase.execute(context, id)
        } catch (e: Exception) {
            println("Error fetching person: ${e.message}")
        }
    }

    private suspend fun fetchExercisesByPerson(context: Context) {
        try {
            _person.value?.let {
                _exerciseList.value = FetchExercisesByPersonUseCase.execute(context, it.id)
            }
        } catch (e: Exception) {
            println("Error fetching person exercises: ${e.message}")
        }
    }

    fun fetchExercisesSearch(
        context: Context,
        id: Int,
        title: String?,
        minWeight: Int?,
        maxWeight: Int?,
        minReps: Int?,
        maxReps: Int?,
        startDate: String?,
        endDate: String?,
    ) {
        try {
            viewModelScope.launch {
                _exerciseList.value = FetchExercisesByPersonWithFiltersUseCase.execute(
                    context,
                    id,
                    title,
                    minWeight,
                    maxWeight,
                    minReps,
                    maxReps,
                    if (startDate?.isEmpty() == true) null else startDate,
                    if (endDate?.isEmpty() == true) null else endDate
                )
            }
        } catch (e: Exception) {
            println("Error fetching exercises with filters: ${e.message}")
        }
    }

    fun editPerson(context: Context, person: Person) {
        try {
            viewModelScope.launch {
                EditPersonUseCase.execute(context, person)
            }
        } catch (e: Exception) {
            println("Error editing person: ${e.message}")
        }
    }

    fun deletePerson(context: Context, id: Int) {
        viewModelScope.launch {
            try {
                DeletePersonUseCase.execute(context, id)
            } catch (e: Exception) {
                println("Error deleting person: ${e.message}")
            }
        }
    }
}