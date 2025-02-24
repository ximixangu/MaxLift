package com.maxlift.data.repository

import com.maxlift.data.datasource.UserDataSource
import com.maxlift.data.model.UserModel
import com.maxlift.domain.model.User
import com.maxlift.domain.repository.IUserRepository
import java.util.UUID

class UserRepository(private val userDataSource: UserDataSource): IUserRepository {

    override fun fetchUserByUUID(uuid: UUID): User? {
        val userModel = userDataSource.getUserByUUID(uuid)
        userModel?.let {
            return userModel.toUserDomain()
        }
        return null
    }

    override fun fetchUserByEmail(email: String): User? {
        val userModel = userDataSource.getUserByMail(email)
        userModel?.let {
            return userModel.toUserDomain()
        }
        return null
    }

    override fun saveUser(user: User) {
        userDataSource.saveUser(user.toUserModel())
    }

    override fun setLoggedUser(user: User) {
        userDataSource.setLoggedUser(user.toUserModel())
    }

    override fun getLoggedUser(): User? {
        return userDataSource.getLoggedUser()?.toUserDomain()
    }

    private fun UserModel.toUserDomain(): User {
        return User(
            uuid = uuid,
            name = name,
            email = email
        )
    }

    private fun User.toUserModel(): UserModel {
        return UserModel(
            uuid = uuid,
            name = name,
            email = email
        )
    }
}