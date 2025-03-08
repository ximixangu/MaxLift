package com.maxlift.domain.usecase.register

import android.util.Patterns

data class RegisterCredentials(
    var email: String = "",
    var name: String= "",
    var password: String= "",
) {
    fun isValid(): Boolean {
        return isNotEmpty() && emailIsValid() && passwordIsValid()
    }

    private fun isNotEmpty(): Boolean {
        return (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty())
    }

    private fun emailIsValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun passwordIsValid(): Boolean {
        return password.length >= 6
    }
}