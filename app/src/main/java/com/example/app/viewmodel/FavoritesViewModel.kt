package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.model.Favorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableStateFlow(emptyList<Favorite>())
    val favorites: StateFlow<List<Favorite>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        // Datos de ejemplo: el usuario "1" tiene como favoritos los lugares "1" y "2"
        _favorites.value = listOf(
            Favorite(
                id = "1",
                userId = "1",
                placeId = "1"
            ),
            Favorite(
                id = "2",
                userId = "1",
                placeId = "2"
            )
        )
    }

    fun getFavoritesByUserId(userId: String): List<Favorite> {
        return _favorites.value.filter { it.userId == userId }
    }

    fun addFavorite(userId: String, placeId: String): Boolean {
        // Verificar que no exista ya el favorito
        val existing = _favorites.value.find { 
            it.userId == userId && it.placeId == placeId
        }
        
        if (existing != null) {
            return false // Ya existe
        }

        val newFavorite = Favorite(
            id = java.util.UUID.randomUUID().toString(),
            userId = userId,
            placeId = placeId
        )
        _favorites.value = _favorites.value + newFavorite
        return true
    }

    fun removeFavorite(userId: String, placeId: String): Boolean {
        val currentFavorites = _favorites.value.toMutableList()
        val favoriteIndex = currentFavorites.indexOfFirst { 
            it.userId == userId && it.placeId == placeId
        }

        if (favoriteIndex != -1) {
            currentFavorites.removeAt(favoriteIndex)
            _favorites.value = currentFavorites
            return true
        }
        return false
    }

    fun isFavorite(userId: String, placeId: String): Boolean {
        return _favorites.value.any { 
            it.userId == userId && it.placeId == placeId
        }
    }
}

