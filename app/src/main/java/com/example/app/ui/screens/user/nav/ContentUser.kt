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
import androidx.compose.ui.platform.LocalContext
import com.example.app.ui.screens.user.tabs.Map
import com.example.app.ui.screens.user.tabs.Favorites
import com.example.app.ui.screens.user.tabs.Places
import com.example.app.ui.screens.user.tabs.PlaceDetail
import com.example.app.ui.screens.user.tabs.CreatePlaceForm
import com.example.app.ui.screens.user.tabs.Friends
import com.example.app.ui.screens.user.tabs.Chat
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
    val messagesViewModel = remember { com.example.app.viewmodel.MessagesViewModel() }
    val favoritesViewModel = remember { com.example.app.viewmodel.FavoritesViewModel() }
    
    NavHost(
        modifier = Modifier.padding(paddingValues = padding),
        navController = navController,
        startDestination = RouteTab.Map
    ) {
        composable<RouteTab.Map> {
            Map(
                placesViewModel = placesViewModel,
                onPlaceClick = { placeId ->
                    navController.navigate(RouteTab.PlaceDetail(placeId))
                }
            )
        }
        
        composable<RouteTab.Favorites> {
            Favorites(
                userId = user?.userId ?: "1",
                favoritesViewModel = favoritesViewModel,
                placesViewModel = placesViewModel,
                onPlaceClick = { placeId ->
                    navController.navigate(RouteTab.PlaceDetail(placeId))
                }
            )
        }
        
        composable<RouteTab.Places> {
            Places(
                placesViewModel = placesViewModel, 
                userId = user?.userId ?: "1",
                onPlaceClick = { placeId ->
                    navController.navigate(RouteTab.PlaceDetail(placeId))
                },
                       onCreatePlace = {
                           navController.navigate(RouteTab.CreatePlace(null))
                       }
            )
        }
        
        composable<RouteTab.PlaceDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteTab.PlaceDetail>()
            PlaceDetail(
                placeId = route.placeId,
                placesViewModel = placesViewModel,
                messagesViewModel = messagesViewModel,
                favoritesViewModel = favoritesViewModel,
                userId = user?.userId ?: "1",
                userRole = user?.role,
                onBack = { navController.popBackStack() },
                onEdit = { placeId ->
                    navController.navigate(RouteTab.CreatePlace(placeId))
                },
                onDelete = {
                    navController.popBackStack()
                }
            )
        }
        
        composable<RouteTab.CreatePlace> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteTab.CreatePlace>()
            CreatePlaceForm(
                userId = user?.userId ?: "1",
                placesViewModel = placesViewModel,
                usersViewModel = usersViewModel,
                placeId = route.placeId,
                onBack = { navController.popBackStack() },
                context = LocalContext.current
            )
        }
        
        composable<RouteTab.Friends> {
            Friends(
                userId = user?.userId ?: "1",
                onFriendClick = { friendId ->
                    navController.navigate(RouteTab.Chat(friendId))
                }
            )
        }
        
        composable<RouteTab.Chat> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteTab.Chat>()
            Chat(
                friendId = route.friendId,
                userId = user?.userId ?: "1",
                messagesViewModel = messagesViewModel,
                placesViewModel = placesViewModel,
                onBack = { navController.popBackStack() },
                onPlaceClick = { placeId ->
                    navController.navigate(RouteTab.PlaceDetail(placeId))
                }
            )
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

