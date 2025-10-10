package com.example.app.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.app.R
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.screens.user.nav.ContentUser
import com.example.app.ui.screens.user.bottombar.BottomBarUser



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUser(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()
    
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
                }
            )
        },
        bottomBar = {
            BottomBarUser(navController = navController)
        }
    ) { paddingValues ->
        ContentUser(
            padding = paddingValues,
            navController = navController,
            onLogout = onLogout
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeUserPreview() {
    AppTheme {
        HomeUser()
    }
}
