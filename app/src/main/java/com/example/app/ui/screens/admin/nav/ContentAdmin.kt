package com.example.app.ui.screens.admin.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.compose.foundation.layout.PaddingValues
import com.example.app.ui.screens.admin.HistoryAdmin
import com.example.app.ui.screens.admin.AuthorizeAdmin
import com.example.app.ui.screens.admin.HomeContentAdmin
import com.example.app.ui.screens.admin.ProfileAdmin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.app.R

@Composable
fun ContentAdmin(
    padding: PaddingValues,
    navController: NavHostController,
    user: com.example.app.model.User?,
    placesViewModel: com.example.app.viewmodel.PlacesViewModel = remember { com.example.app.viewmodel.PlacesViewModel() },
    onLogout: () -> Unit = {}
) {
    NavHost(
        modifier = Modifier.padding(paddingValues = padding),
        navController = navController,
        startDestination = RouteTabAdmin.Home
    ) {
        composable<RouteTabAdmin.Home> {
            HomeContentAdmin(
                placesViewModel = placesViewModel,
                navController = navController
            )
        }
        
        composable<RouteTabAdmin.History> {
            HistoryAdmin(
                placesViewModel = placesViewModel,
                navController = navController
            )
        }
        
        composable<RouteTabAdmin.Authorize> {
            AuthorizeAdmin(
                placesViewModel = placesViewModel,
                navController = navController
            )
        }
        
        composable<RouteTabAdmin.PlaceDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteTabAdmin.PlaceDetail>()
            com.example.app.ui.screens.user.tabs.PlaceDetail(
                placeId = route.placeId,
                placesViewModel = placesViewModel,
                messagesViewModel = remember { com.example.app.viewmodel.MessagesViewModel() },
                favoritesViewModel = remember { com.example.app.viewmodel.FavoritesViewModel() },
                userId = user?.userId ?: "3",
                userRole = user?.role,
                onBack = { navController.popBackStack() },
                onEdit = { },
                onDelete = { }
            )
        }
        
        composable<RouteTabAdmin.Profile> {
            ProfileAdmin(
                userName = user?.name ?: "Administrador",
                userUsername = user?.username ?: "moderador",
                userEmail = user?.email ?: "moderador@email.com",
                userCity = user?.city ?: "Armenia",
                onLogout = onLogout
            )
        }
    }
}
