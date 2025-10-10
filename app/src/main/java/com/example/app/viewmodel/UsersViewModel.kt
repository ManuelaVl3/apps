package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.model.Role
import com.example.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.String

class UsersViewModel: ViewModel() {

    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User> > = _users.asStateFlow()

    init{
        loadUsers()
    }

    fun loadUsers(){

        _users.value = listOf(
            User(
                "1",
                "Manuela",
                "Manuela_Vl3",
                "manuelav@email.com",
                "1234567890-",
                "Armenia",
                Role.USER,

            ),

            User(
                "2",
                "Pascal",
                "Pascalito2511",
                "pascal@email.com",
                "1234567890-",
                "Medellín",
                Role.USER,

            ),

            User(
                "3",
                "Admin",
                "Admin",
                "admin@email.com",
                "1234567890-",
                "Cartagena",
                Role.ADMIN,

                )
        )
    }

    fun create(user: User){
        _users.value = _users.value + user
    }

    fun findById(userId: String): User?{
        return _users.value.find { it.userId == userId }
    }

    fun findByEmail(email: String): User?{
        return _users.value.find { it.email == email }
    }

    fun login(email : String, password: String): User?{
        return _users.value.find {it.email == email && it.password== password}
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
                // email y password se mantienen sin cambios
            )
            _users.value = currentUsers
            return true
        }
        return false
    }

}