package com.maxlift.domain.usecase.login

import android.util.Patterns

data class Credentials(
    var email: String = "",
    var password: String = ""
){
    fun isValid(): Boolean {
        return isNotEmpty() && emailIsValid() && passwordIsValid()
    }

    private fun isNotEmpty(): Boolean {
        return (email.isNotEmpty() && password.isNotEmpty())
    }

    private fun emailIsValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun passwordIsValid(): Boolean {
        return password.length >= 6
    }
}
