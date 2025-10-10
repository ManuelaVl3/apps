package com.example.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.config.RouteScreen
import com.example.app.ui.screens.admin.HomeAdmin
import com.example.app.ui.screens.user.HomeUser
import com.example.app.ui.screens.user.MyPlaces
import com.example.app.ui.screens.user.Favorites
import com.example.app.ui.screens.user.Friends

@Composable
fun Navigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = RouteScreen.Home
    ) {
        composable<RouteScreen.Home> {
            MainPage(
                onLogin = { navController.navigate(RouteScreen.Login) },
                onSignUp = { navController.navigate(RouteScreen.Register) }
            )
        }
        
        composable<RouteScreen.Login> {
            LoginForm(
                onRegister = { navController.navigate(RouteScreen.Register) },
                onLoginSuccess = { navController.navigate(RouteScreen.HomeUser) }
            )
        }
        
        composable<RouteScreen.Register> {
            RegisterForm(
                onRegister = { },
                onLoginClick = { navController.navigate(RouteScreen.Login) }
            )
        }
        
        composable<RouteScreen.HomeUser> {
            HomeUser(
                onNavigateToMyPlaces = { navController.navigate(RouteScreen.MyPlaces) },
                onNavigateToFavorites = { navController.navigate(RouteScreen.Favorites) },
                onNavigateToFriends = { navController.navigate(RouteScreen.Friends) },
                onNavigateToProfile = { navController.navigate(RouteScreen.EditProfile) },
                onBack = { 
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Home) { inclusive = true }
                    }
                }
            )
        }
        
        composable<RouteScreen.MyPlaces> {
            MyPlaces()
        }
        
        composable<RouteScreen.Favorites> {
            Favorites()
        }
        
        composable<RouteScreen.Friends> {
            Friends()
        }
        
        composable<RouteScreen.EditProfile> {
            EditProfile(
                onLogout = { 
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Home) { inclusive = true }
                    }
                }
            )
        }
    }
}
