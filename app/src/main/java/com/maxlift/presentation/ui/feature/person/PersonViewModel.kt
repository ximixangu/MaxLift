package com.maxlift.presentation.ui.feature.person

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Person
import com.maxlift.domain.usecase.person.FetchAllPersonsUseCase
import com.maxlift.domain.usecase.person.FetchPersonUseCase
import kotlinx.coroutines.launch

class PersonViewModel: ViewModel() {
    private var _personList = MutableLiveData<List<Person>>()
    val personListState: LiveData<List<Person>> = _personList

    private var _person = MutableLiveData<Person>()
    val personState: LiveData<Person> = _person

    fun fetchAllPersons(context: Context) {
        viewModelScope.launch {
            try {
                _personList.value = FetchAllPersonsUseCase.execute(context)
            } catch (e: Exception) {
                println("Error fetching all persons: ${e.message}")
            }
        }
    }

    fun fetchPersonById(context: Context, id: Int) {
        viewModelScope.launch {
            try {
                _person.value = FetchPersonUseCase.execute(context, id)
            } catch (e: Exception) {
                println("Error fetching person: ${e.message}")
            }
        }
    }
}