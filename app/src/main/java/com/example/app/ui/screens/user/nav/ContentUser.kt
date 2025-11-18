package com.example.app.ui.screens.user.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.compose.foundation.layout.PaddingValues
import com.example.app.ui.screens.user.tabs.Map
import com.example.app.ui.screens.user.tabs.Search
import com.example.app.ui.screens.user.tabs.Places
import com.example.app.ui.screens.user.tabs.PlaceDetail
import com.example.app.ui.screens.user.tabs.CreatePlaceForm
import com.example.app.ui.screens.EditProfile
import com.example.app.viewmodel.UsersViewModel

@Composable
fun ContentUser(
    padding: PaddingValues,
    navController: NavHostController,
    placesViewModel: com.example.app.viewmodel.PlacesViewModel,
    user: com.example.app.model.User?,
    onLogout: () -> Unit = {}
) {
    val usersViewModel = remember { UsersViewModel() }
    
    NavHost(
        modifier = Modifier.padding(paddingValues = padding),
        navController = navController,
        startDestination = RouteTab.Map
    ) {
        composable<RouteTab.Map> {
            Map()
        }
        
        composable<RouteTab.Search> {
            Search()
        }
        
        composable<RouteTab.Places> {
            Places(
                placesViewModel = placesViewModel, 
                userId = user?.userId ?: "1",
                onPlaceClick = { placeId ->
                    navController.navigate(RouteTab.PlaceDetail(placeId))
                },
                onCreatePlace = {
                    navController.navigate(RouteTab.CreatePlace)
                }
            )
        }
        
        composable<RouteTab.PlaceDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteTab.PlaceDetail>()
            PlaceDetail(
                placeId = route.placeId,
                placesViewModel = placesViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable<RouteTab.CreatePlace> {
            CreatePlaceForm(
                userId = user?.userId ?: "1",
                placesViewModel = placesViewModel,
                usersViewModel = usersViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable<RouteTab.Friends> {
            com.example.app.ui.screens.user.Friends()
        }
        
        composable<RouteTab.Profile> {
            EditProfile(
                userName = user?.name ?: "Usuario",
                userUsername = user?.username ?: "",
                userCity = user?.city ?: "Armenia",
                onLogout = onLogout
            )
        }
    }
}

