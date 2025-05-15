package com.maxlift.presentation.ui.view.person.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Person
import com.maxlift.domain.usecase.person.DeletePersonUseCase
import com.maxlift.domain.usecase.person.EditPersonUseCase
import com.maxlift.domain.usecase.person.FetchAllPersonsUseCase
import com.maxlift.domain.usecase.person.FetchPersonUseCase
import com.maxlift.domain.usecase.person.SavePersonUseCase
import kotlinx.coroutines.launch

class PersonViewModel(
    private val fetchAllPersonsUseCase: FetchAllPersonsUseCase,
    private val fetchPersonUseCase: FetchPersonUseCase,
    private val deletePersonUseCase: DeletePersonUseCase,
    private val editPersonUseCase: EditPersonUseCase,
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

    fun editPerson(person: Person) {
        try {
            viewModelScope.launch {
                editPersonUseCase(person)
                fetchPersonById(person.id)
            }
        } catch (e: Exception) {
            println("Error editing person: ${e.message}")
        }
    }

    fun deletePerson(id: Int) {
        viewModelScope.launch {
            try {
                deletePersonUseCase(id)
            } catch (e: Exception) {
                println("Error deleting person: ${e.message}")
            }
        }
    }

    fun savePerson(person: Person) {
        viewModelScope.launch {
            try {
                savePersonUseCase(person)
                fetchAllPersons()
            } catch (e: Exception) {
                println("Error saving person: ${e.message}")
            }
        }
    }
}