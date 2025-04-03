package com.maxlift.presentation.ui.feature.person

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Person
import com.maxlift.domain.usecase.person.FetchAllPersonsUseCase
import kotlinx.coroutines.launch

class PersonViewModel: ViewModel() {
    private var _personList = MutableLiveData<List<Person>>()
    val personListState: LiveData<List<Person>> = _personList

    fun fetchAllPersons(context: Context) {
        viewModelScope.launch {
            try {
                _personList.value = FetchAllPersonsUseCase.execute(context)
            } catch (e: Exception) {
                println("Error fetching all persons: ${e.message}")
            }
        }
    }
}