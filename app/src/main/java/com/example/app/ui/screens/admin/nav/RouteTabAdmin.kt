package com.example.app.ui.screens.admin.nav

import kotlinx.serialization.Serializable

sealed class RouteTabAdmin {
    @Serializable
    data object Home : RouteTabAdmin()

    @Serializable
    data object History : RouteTabAdmin()

    @Serializable
    data object Authorize : RouteTabAdmin()

    @Serializable
    data object Profile : RouteTabAdmin()

    @Serializable
    data class PlaceDetail(val placeId: String) : RouteTabAdmin()
}
