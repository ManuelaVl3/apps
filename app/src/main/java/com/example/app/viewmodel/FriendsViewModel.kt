package com.example.app.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.app.model.Friend
import com.example.app.model.FriendStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendsViewModel : ViewModel() {

    private val _friends = MutableStateFlow(emptyList<Friend>())
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()

    init {
        loadFriends()
    }

    fun loadFriends() {
        // Datos de ejemplo: el usuario "1" tiene como amigo a "2"
        // Nota: "3" es moderador, por lo que no puede ser amigo
        _friends.value = listOf(
            Friend(
                id = "1",
                userId = "1",
                friendId = "2",
                status = FriendStatus.ACCEPTED
            )
        )
    }

    fun getFriendsByUserId(userId: String): List<Friend> {
        return _friends.value.filter { 
            it.userId == userId && it.status == FriendStatus.ACCEPTED 
        }
    }

    fun addFriend(userId: String, friendId: String): Boolean {
        val existing = _friends.value.find { 
            (it.userId == userId && it.friendId == friendId) ||
            (it.userId == friendId && it.friendId == userId)
        }
        
        if (existing != null) {
            return false
        }

        val newFriend = Friend(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            friendId = friendId,
            status = FriendStatus.ACCEPTED
        )
        _friends.value = _friends.value + newFriend
        return true
    }

    fun removeFriend(userId: String, friendId: String): Boolean {
        val currentFriends = _friends.value.toMutableList()
        val friendIndex = currentFriends.indexOfFirst { 
            (it.userId == userId && it.friendId == friendId) ||
            (it.userId == friendId && it.friendId == userId)
        }

        if (friendIndex != -1) {
            currentFriends.removeAt(friendIndex)
            _friends.value = currentFriends
            return true
        }
        return false
    }

    fun isFriend(userId: String, friendId: String): Boolean {
        return _friends.value.any { 
            (it.userId == userId && it.friendId == friendId) ||
            (it.userId == friendId && it.friendId == userId)
        } && _friends.value.find { 
            (it.userId == userId && it.friendId == friendId) ||
            (it.userId == friendId && it.friendId == userId)
        }?.status == FriendStatus.ACCEPTED
    }
}

