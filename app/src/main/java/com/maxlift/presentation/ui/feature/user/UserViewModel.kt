package com.maxlift.presentation.ui.feature.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxlift.domain.model.User
import com.maxlift.domain.usecase.login.GetLoggedUserUseCase

class UserViewModel(private val getUserUseCase: GetLoggedUserUseCase) : ViewModel() {
    private val _userState = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _userState

    fun loadUser() {
        val user = getUserUseCase.execute()
        _userState.value = user
    }
}