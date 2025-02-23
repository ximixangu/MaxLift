package com.maxlift.domain.usecase.login

data class Credentials(
    var email: String = "",
    var password: String = ""
){
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}
