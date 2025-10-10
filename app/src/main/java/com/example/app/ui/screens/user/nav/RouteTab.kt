package com.example.app.ui.screens.user.nav

import kotlinx.serialization.Serializable

sealed class RouteTab {
    @Serializable
    data object Home : RouteTab()

    @Serializable
    data object MyPlaces : RouteTab()

    @Serializable
    data object Favorites : RouteTab()

    @Serializable
    data object Friends : RouteTab()

    @Serializable
    data object Profile : RouteTab()
}