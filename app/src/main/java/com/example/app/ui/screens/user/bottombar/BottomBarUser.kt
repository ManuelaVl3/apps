package com.example.app.ui.screens.user.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
import com.example.app.ui.screens.user.nav.RouteTab

sealed class DestinationIcon {
    data class VectorIcon(val icon: ImageVector) : DestinationIcon()
    data class DrawableIcon(val resId: Int) : DestinationIcon()
}

enum class Destination(val titleResId: Int, val icon: DestinationIcon, val route: RouteTab) {
    Map(R.string.home, DestinationIcon.VectorIcon(Icons.Default.Home), RouteTab.Map),
    Favorites(R.string.favorites, DestinationIcon.DrawableIcon(R.drawable.favorite_24), RouteTab.Favorites),
    Places(R.string.my_places, DestinationIcon.VectorIcon(Icons.Default.LocationOn), RouteTab.Places),
    Friends(R.string.friends, DestinationIcon.VectorIcon(Icons.Default.Group), RouteTab.Friends),
    Profile(R.string.profile, DestinationIcon.VectorIcon(Icons.Default.Person), RouteTab.Profile)
}

@Composable
fun BottomBarUser(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = OrangeDeep,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Destination.values().forEachIndexed { index, destination ->
            val isSelected = currentDestination?.route == destination.route::class.qualifiedName

            NavigationBarItem(
                modifier = Modifier.weight(1f),
                icon = {
                    when (val icon = destination.icon) {
                        is DestinationIcon.VectorIcon -> {
                            Icon(
                                imageVector = icon.icon,
                                contentDescription = stringResource(destination.titleResId),
                                tint = if (isSelected) Orange else Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        is DestinationIcon.DrawableIcon -> {
                            Image(
                                painter = painterResource(id = icon.resId),
                                contentDescription = stringResource(destination.titleResId),
                                colorFilter = ColorFilter.tint(if (isSelected) Orange else Color.White),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
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
