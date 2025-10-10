package com.example.app.model

data class User (
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val city: String,
    val role: Role
){
}