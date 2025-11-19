package com.example.app.ui.screens.admin.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.PaddingValues
import com.example.app.ui.screens.EditProfile
import com.example.app.ui.screens.admin.HistoryAdmin
import com.example.app.ui.screens.admin.AuthorizeAdmin
import com.example.app.ui.screens.admin.HomeContentAdmin
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
            HomeContentAdmin(placesViewModel = placesViewModel)
        }
        
        composable<RouteTabAdmin.History> {
            HistoryAdmin()
        }
        
        composable<RouteTabAdmin.Authorize> {
            AuthorizeAdmin(placesViewModel = placesViewModel)
        }
        
        composable<RouteTabAdmin.Profile> {
            EditProfile(
                userName = user?.name ?: "Administrador",
                userUsername = user?.username ?: "",
                userCity = user?.city ?: "Armenia",
                onLogout = onLogout
            )
        }
    }
}
