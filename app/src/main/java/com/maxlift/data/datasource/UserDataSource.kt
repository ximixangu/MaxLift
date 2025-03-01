package com.maxlift.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.maxlift.data.model.UserModel
import java.util.UUID

class UserDataSource private constructor(){
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: UserDataSource? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) { instance ?: UserDataSource().also {
                instance = it
                it.context = context
                it.loadUserData()
                }
            }
    }

    private var context: Context? = null
    private var loggedUser: UserModel? = null
    private val usersUUIDMap: MutableMap<UUID, UserModel> = HashMap()
    private val usersMailMap: MutableMap<String, UserModel> = HashMap()

    fun loadUserData() { context?.let {
        val stringDataFromRawAsset: String? = AssetsProvider.getJsonDataFromRawAsset(it)
        stringDataFromRawAsset?.let { str ->
            val user: UserModel? = parseJson(str)
                user?.let{
                    this.saveUser(user)
                }
            }
        }
    }

    private fun parseJson(json: String): UserModel? {
        val gson = GsonBuilder().create()
        return try {
            gson.fromJson(json, UserModel::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    fun saveUser(user: UserModel){
        if(!usersMailMap.containsKey(user.email) && !usersUUIDMap.containsKey(user.uuid)){
            usersUUIDMap[user.uuid] = user
            usersMailMap[user.email] = user
            println(user.email)
        }
    }

    fun getUserByUUID(uuid: UUID): UserModel? {
        return usersUUIDMap[uuid]
    }

    fun getUserByMail(mail: String): UserModel?{
        return usersMailMap[mail]
    }

    fun setLoggedUser(user: UserModel) {
        loggedUser = user
    }

    fun getLoggedUser(): UserModel? {
        return loggedUser
    }
}