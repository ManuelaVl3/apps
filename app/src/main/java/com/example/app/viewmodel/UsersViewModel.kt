package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.model.Role
import com.example.app.model.User
import com.example.app.utils.RequestResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.String

class UsersViewModel : ViewModel() {

    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _userResult = MutableStateFlow<RequestResult?>(null)
    val userResult: StateFlow<RequestResult?> = _userResult.asStateFlow()

    val db = Firebase.firestore

    init {
        loadUsers()
    }

    fun create(user: User) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching {
                createFirebase(user)
            }.fold(
                onSuccess = { RequestResult.Success("User created successfully") },
                onFailure = { RequestResult.Failure(it.message ?: "Error creating user") }
            )
        }
    }

    private suspend fun createFirebase(user: User) {
        db.collection("users").add(user).await()
    }

    fun resetOperationResult() {
        _userResult.value = null
    }

    fun loadUsers() {

        _users.value = listOf(
            User(
                "1",
                "Manuela",
                "Manuela_Vl3",
                "manuela@email.com",
                "1234567890*",
                "Armenia",
                Role.USER
            ),

            User(
                "2",
                "Pascal",
                "Pascalito2511",
                "pascal@email.com",
                "12345678",
                "Medellín",
                Role.USER,

                ),

            User(
                "3",
                "Moderador",
                "Moderador_Admin",
                "moderador@mod.com",
                "0987654321!",
                "Armenia",
                Role.ADMIN
            )
        )
    }

    fun findById(userId: String): User? {
        return _users.value.find { it.userId == userId }
    }

    fun findByUserId(userId: String): User? {
        return _users.value.find { it.userId == userId }
    }

    fun findByEmail(email: String): User? {
        return _users.value.find { it.email == email }
    }

    fun login(email: String, password: String): User? {
        return _users.value.find { it.email == email && it.password == password }
    }

    fun updateUser(
        userId: String,
        name: String,
        username: String,
        city: String
    ): Boolean {
        val currentUsers = _users.value.toMutableList()
        val userIndex = currentUsers.indexOfFirst { it.userId == userId }

        if (userIndex != -1) {
            val existingUser = currentUsers[userIndex]
            currentUsers[userIndex] = existingUser.copy(
                name = name,
                username = username,
                city = city
            )
            _users.value = currentUsers
            return true
        }
        return false
    }

}