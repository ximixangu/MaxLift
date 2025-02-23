package com.maxlift.domain.usecase.register

data class RegisterCredentials(
    var email: String = "",
    var name: String= "",
    var password: String= "")
{
    fun isNotEmpty(): Boolean{
        return (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty())
    }
}