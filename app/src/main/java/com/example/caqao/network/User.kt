package com.example.caqao.network

data class User(
    val first_name: String = "",
    val last_name: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
)

data class UserAccountCreationStatus(
    val message: String = "",
    val status: Int? = null
)

data class UserLoginStatus(
    val message: String = "",
    val status: Int? = null,
    val token: String = "",
    val email: String = "",
    val username: String = "",
)
