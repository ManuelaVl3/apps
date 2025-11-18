package com.example.app.ui.screens.user.nav

import kotlinx.serialization.Serializable

sealed class RouteTab {
    @Serializable
    data object Map : RouteTab()

    @Serializable
    data object Search : RouteTab()

    @Serializable
    data object Places : RouteTab()

    @Serializable
    data object Friends : RouteTab()

    @Serializable
    data object Profile : RouteTab()
    
    @Serializable
    data class PlaceDetail(val placeId: String) : RouteTab()
    
    @Serializable
    data object CreatePlace : RouteTab()
}