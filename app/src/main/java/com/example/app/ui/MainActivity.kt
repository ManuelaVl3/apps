package com.example.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.app.ui.screens.MainPage
import com.example.app.ui.screens.LoginForm
import com.example.app.ui.screens.RegisterForm
import com.example.app.ui.screens.EditProfile
import com.example.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                var currentScreen by remember { mutableStateOf("main") }
                
                when (currentScreen) {
                    "main" -> MainPage(
                        onLogin = { currentScreen = "login" },
                        onSignUp = { currentScreen = "register" }
                    )
                    "login" -> LoginForm(
                        onRegister = { currentScreen = "register" },
                        onLoginSuccess = { currentScreen = "editProfile" }
                    )
                    "register" -> RegisterForm(
                        onRegister = { },
                        onLoginClick = { currentScreen = "login" }
                    )
                    "editProfile" -> EditProfile(
                        onLogout = { currentScreen = "main" }
                    )
                }
            }
        }
    }
}
