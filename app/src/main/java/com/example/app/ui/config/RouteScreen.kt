package com.example.app.ui.config

import kotlinx.serialization.Serializable

sealed class RouteScreen {
    @Serializable
    data object Home : RouteScreen()
    
    @Serializable
    data object Login : RouteScreen()
    
    @Serializable
    data object Register : RouteScreen()
    
    @Serializable
    data object HomeUser : RouteScreen()
    
    @Serializable
    data object MyPlaces : RouteScreen()
    
    @Serializable
    data object Favorites : RouteScreen()
    
    @Serializable
    data object Friends : RouteScreen()
    
    @Serializable
    data object EditProfile : RouteScreen()

    @Serializable
    data object HomeAdmin : RouteScreen()
}
