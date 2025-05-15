package com.maxlift.presentation.ui.view.person.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Person
import com.maxlift.domain.usecase.person.FetchAllPersonsUseCase
import com.maxlift.domain.usecase.person.FetchPersonUseCase
import com.maxlift.domain.usecase.person.SavePersonUseCase
import kotlinx.coroutines.launch

class PersonViewModel(
    private val fetchAllPersonsUseCase: FetchAllPersonsUseCase,
    private val fetchPersonUseCase: FetchPersonUseCase,
    private val savePersonUseCase: SavePersonUseCase,
): ViewModel() {
    private var _personList = MutableLiveData<List<Person>>()
    val personListState: LiveData<List<Person>> = _personList

    private var _person = MutableLiveData<Person?>()
    val personState: LiveData<Person?> = _person

    fun fetchAllPersons() {
        viewModelScope.launch {
            try {
                _personList.value = fetchAllPersonsUseCase()
            } catch (e: Exception) {
                println("Error fetching all persons: ${e.message}")
            }
        }
    }

    fun fetchPersonById(id: Int) {
        viewModelScope.launch {
            try {
                _person.value = fetchPersonUseCase(id)
            } catch (e: Exception) {
                println("Error fetching person: ${e.message}")
            }
        }
    }

    fun savePerson(person: Person) {
        viewModelScope.launch {
            try {
                savePersonUseCase(person)
            } catch (e: Exception) {
                println("Error fetching person: ${e.message}")
            }
        }
    }
}