package com.example.app.ui.screens.admin.bottombar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app.R
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.screens.admin.nav.RouteTabAdmin

enum class DestinationAdmin(val titleResId: Int, val icon: ImageVector, val route: RouteTabAdmin) {
    Home(R.string.home, Icons.Default.Home, RouteTabAdmin.Home),
    History(R.string.history, Icons.Default.History, RouteTabAdmin.History),
    Authorize(R.string.authorize, Icons.Default.CheckCircle, RouteTabAdmin.Authorize),
    Profile(R.string.profile, Icons.Default.Person, RouteTabAdmin.Profile)
}

@Composable
fun BottomBarAdmin(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = OrangeDeep,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        DestinationAdmin.values().forEachIndexed { index, destination ->
            val isSelected = currentDestination?.route == destination.route::class.qualifiedName

            NavigationBarItem(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.titleResId),
                        tint = if (isSelected) Orange else Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.titleResId),
                        fontSize = 11.sp,
                        color = if (isSelected) Orange else Color.White,
                        fontFamily = MontserratFamily,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(destination.route)
                }
            )
        }
    }
}
