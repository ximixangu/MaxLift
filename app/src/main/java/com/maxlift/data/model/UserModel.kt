package com.maxlift.data.model

import java.util.UUID

data class UserModel(
    val uuid: UUID,
    val name: String,
    val email: String,
)