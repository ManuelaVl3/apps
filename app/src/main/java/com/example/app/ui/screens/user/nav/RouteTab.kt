package com.example.app.ui.screens.user.nav

import kotlinx.serialization.Serializable

sealed class RouteTab {
    @Serializable
    data object Map : RouteTab()

    @Serializable
    data object Favorites : RouteTab()

    @Serializable
    data object Places : RouteTab()

    @Serializable
    data object Friends : RouteTab()

    @Serializable
    data object Profile : RouteTab()
    
    @Serializable
    data class PlaceDetail(val placeId: String) : RouteTab()
    
    @Serializable
    data class CreatePlace(val placeId: String? = null) : RouteTab()
    
    @Serializable
    data class Chat(val friendId: String) : RouteTab()
}