package com.example.app.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.MontserratFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUser(
    onNavigateToMyPlaces: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var selectedItem by remember { mutableStateOf(0) }
    
    val bottomNavItems = listOf(
        BottomNavItem(stringResource(R.string.home), Icons.Default.Home, 0),
        BottomNavItem(stringResource(R.string.my_places), Icons.Default.LocationOn, 1),
        BottomNavItem(stringResource(R.string.favorites), Icons.Default.Favorite, 2),
        BottomNavItem(stringResource(R.string.friends), Icons.Default.People, 3),
        BottomNavItem(stringResource(R.string.profile), Icons.Default.Person, 4)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                append(stringResource(R.string.user_greating))
                            }
                            withStyle(style = SpanStyle(color = OrangeDeep)) {
                                append(stringResource(R.string.name_user_greating))
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                append(stringResource(R.string.user_wave))
                            }
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = MontserratFamily
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = OrangeDeep,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        modifier = Modifier.weight(1f),
                        icon = { 
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        label = { 
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                color = Color.White,
                                fontFamily = MontserratFamily,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        },
                        selected = selectedItem == item.index,
                        onClick = { 
                            selectedItem = item.index
                            when (item.index) {
                                0 -> { }
                                1 -> onNavigateToMyPlaces()
                                2 -> onNavigateToFavorites()
                                3 -> onNavigateToFriends()
                                4 -> onNavigateToProfile()
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.user_subtitle),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontFamily = MontserratFamily
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val index: Int
)

@Preview(showBackground = true)
@Composable
private fun HomeUserPreview() {
    AppTheme {
        HomeUser()
    }
}
