package com.maxlift.domain.repository

import com.maxlift.domain.model.User
import java.util.UUID

interface IUserRepository {
    fun fetchUserByUUID(uuid: UUID): User?
    fun fetchUserByEmail(email: String): User?
    fun saveUser(user: User)
    fun setLoggedUser(user: User)
    fun getLoggedUser(): User?
}