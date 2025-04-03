package com.maxlift.presentation.ui.feature.person.info

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.model.Person
import com.maxlift.domain.usecase.exercise.FetchExercisesByPersonUseCase
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
}