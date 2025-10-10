package com.example.app.ui.screens.user.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.PaddingValues
import com.example.app.ui.screens.EditProfile
import com.example.app.ui.screens.user.Favorites
import com.example.app.ui.screens.user.Friends
import com.example.app.ui.screens.user.MyPlaces
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
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
fun ContentUser(
    padding: PaddingValues,
    navController: NavHostController,
    onLogout: () -> Unit = {}
) {
    NavHost(
        modifier = Modifier.padding(paddingValues = padding),
        navController = navController,
        startDestination = RouteTab.Home
    ) {
        composable<RouteTab.Home> {
            HomeContent()
        }
        
        composable<RouteTab.MyPlaces> {
            MyPlaces()
        }
        
        composable<RouteTab.Favorites> {
            Favorites()
        }
        
        composable<RouteTab.Friends> {
            Friends()
        }
        
        composable<RouteTab.Profile> {
            EditProfile(
                onLogout = onLogout
            )
        }
    }
}

@Composable
fun HomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = androidx.compose.ui.res.stringResource(com.example.app.R.string.user_subtitle),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontFamily = com.example.app.ui.theme.MontserratFamily
        )
    }
}
