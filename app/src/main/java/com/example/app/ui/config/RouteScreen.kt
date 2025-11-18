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
    data object ForgotPassword : RouteScreen()
    
    @Serializable
    data class HomeUser(val userId: String) : RouteScreen()
    

    @Serializable
    data class HomeAdmin(val userId: String) : RouteScreen()
}
