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
    onLogout: () -> Unit = {}
) {
    NavHost(
        modifier = Modifier.padding(paddingValues = padding),
        navController = navController,
        startDestination = RouteTabAdmin.Home
    ) {
        composable<RouteTabAdmin.Home> {
            HomeContentAdmin()
        }
        
        composable<RouteTabAdmin.History> {
            HistoryAdmin()
        }
        
        composable<RouteTabAdmin.Authorize> {
            AuthorizeAdmin()
        }
        
        composable<RouteTabAdmin.Profile> {
            EditProfile(
                onLogout = onLogout
            )
        }
    }
}

@Composable
fun HomeContentAdmin() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.admin_panel_main),
            fontSize = 24.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontFamily = com.example.app.ui.theme.MontserratFamily
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            text = stringResource(R.string.admin_welcome),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontFamily = com.example.app.ui.theme.MontserratFamily
        )
    }
}
