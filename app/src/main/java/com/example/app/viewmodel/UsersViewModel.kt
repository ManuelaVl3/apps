package com.example.app.viewmodel

import android.util.Log
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
    val db = Firebase.firestore
    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _userResult = MutableStateFlow<RequestResult?>(null)
    val userResult: StateFlow<RequestResult?> = _userResult.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userLogged = MutableStateFlow<User?>(null)
    val userLogged: StateFlow<User?> = _userLogged.asStateFlow()

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

    fun findByUserId(userId: String) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching {
                findByUserIdFirebase(userId)
            }.fold(
                onSuccess = { RequestResult.Success("User got successfully") },
                onFailure = { RequestResult.Failure(it.message ?: "Error getting user") }
            )
        }
    }

    private suspend fun findByUserIdFirebase(userId: String) {
        val snapshot = db.collection("users").document(userId).get().await()

        val user = snapshot.toObject(User::class.java)?.apply {
            this.userId = userId
        }

        _currentUser.value = user
    }

    fun findByEmail(email: String): User? {
        return _users.value.find { it.email == email }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching {
                loginFirebase(email, password)
            }.fold(
                onSuccess = { RequestResult.Success("login successfully") },
                onFailure = { RequestResult.Failure(it.message ?: "Error login") }
            )
        }
    }

    private suspend fun loginFirebase(email: String, password: String) {
        val snapshot = db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()

        if (snapshot.documents.isEmpty()) throw Exception("User or password incorrect")

        snapshot.documents.mapNotNull {
            val user = it.toObject(User::class.java)?.apply {
                this.userId = it.id
            }

            Log.d("FIREBASEEEEEEEEEEE", "loginFirebase: $user")


            _userLogged.value = user
        }
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