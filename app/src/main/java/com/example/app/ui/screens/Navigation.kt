package com.example.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.app.ui.config.RouteScreen
import com.example.app.ui.screens.admin.HomeAdmin
import com.example.app.ui.screens.user.HomeUser

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
                onLoginSuccess = { userId -> navController.navigate(RouteScreen.HomeUser(userId)) },
                onAdminLoginSuccess = { userId -> navController.navigate(RouteScreen.HomeAdmin(userId)) },
                onForgotPassword = { navController.navigate(RouteScreen.ForgotPassword) }
            )
        }
        
        composable<RouteScreen.Register> {
            RegisterForm(
                onRegister = { },
                onLoginClick = { navController.navigate(RouteScreen.Login) }
            )
        }
        
        composable<RouteScreen.ForgotPassword> {
            ForgotPassword(
                onBack = { navController.navigate(RouteScreen.Login) }
            )
        }
        
        composable<RouteScreen.HomeUser> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteScreen.HomeUser>()
            HomeUser(
                userId = route.userId,
                onBack = { 
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Home) { inclusive = true }
                    }
                },
                onLogout = {
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Home) { inclusive = true }
                    }
                }
            )
        }
        
        composable<RouteScreen.HomeAdmin> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteScreen.HomeAdmin>()
            HomeAdmin(
                userId = route.userId,
                onBack = { 
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Home) { inclusive = true }
                    }
                },
                onLogout = {
                    navController.navigate(RouteScreen.Home) {
                        popUpTo(RouteScreen.Home) { inclusive = true }
                    }
                }
            )
        }
    }
}
