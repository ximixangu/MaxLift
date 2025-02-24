package com.maxlift.presentation.ui.activities.profileActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxlift.domain.model.User
import com.maxlift.domain.usecase.GetLoggedUserUseCase

class UserViewModel(private val getUserUseCase: GetLoggedUserUseCase) : ViewModel() {
    private val _userState = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _userState

    fun loadUser() {
        println(user.value)
        val user = getUserUseCase.execute()
        user?.let { println(user.name) }
        _userState.value = user
    }
}