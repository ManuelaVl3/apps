package com.example.app.model

data class User (
    var userId: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val city: String = "",
    val role: Role = Role.USER
)