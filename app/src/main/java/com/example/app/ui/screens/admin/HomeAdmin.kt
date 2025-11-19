package com.example.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.screens.admin.nav.ContentAdmin
import com.example.app.ui.screens.admin.bottombar.BottomBarAdmin
import com.example.app.viewmodel.UsersViewModel

@Composable
fun HomeAdmin(
    userId: String = "3",
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()
    val usersViewModel = remember { UsersViewModel() }
    val placesViewModel = remember { com.example.app.viewmodel.PlacesViewModel() }

    val userLogged by usersViewModel.currentUser.collectAsState()

    
    val user = usersViewModel.findByUserId(userId)
    
    Scaffold(
        bottomBar = {
            BottomBarAdmin(navController = navController)
        }
    ) { paddingValues ->
        ContentAdmin(
            padding = paddingValues,
            navController = navController,
            user = userLogged,
            placesViewModel = placesViewModel,
            onLogout = onLogout
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeAdminPreview() {
    AppTheme {
        HomeAdmin()
    }
}
