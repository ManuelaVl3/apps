package com.example.app.ui.screens.user.tabs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app.R
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.screens.user.nav.ContentUser
import com.example.app.ui.screens.user.bottombar.BottomBarUser
import com.example.app.viewmodel.PlacesViewModel
import com.example.app.viewmodel.UsersViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUser(
    userId: String = "1",
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val placesViewModel = remember { PlacesViewModel() }
    val usersViewModel = remember { UsersViewModel() }
    usersViewModel.findByUserId(userId)
    val user by usersViewModel.userLogged.collectAsState()
    
    // Estado para controlar si se muestran los permisos
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val hasRequestedLocationPermission = remember {
        prefs.getBoolean("has_requested_location_permission_$userId", false)
    }
    var showPermissionDialog by remember { mutableStateOf(!hasRequestedLocationPermission) }
    
    // Verificar permisos actuales
    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    // Launcher para solicitar permisos
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (fineLocationGranted || coarseLocationGranted) {
            // Permisos concedidos
            prefs.edit().putBoolean("has_requested_location_permission_$userId", true).apply()
        }
        showPermissionDialog = false
    }
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val showTopBar = currentDestination?.contains("Map") == true || currentDestination == null
    
    // Solicitar permisos cuando se muestra el diálogo por primera vez
    LaunchedEffect(showPermissionDialog) {
        if (showPermissionDialog && !hasLocationPermission) {
            // El diálogo se mostrará primero, luego se solicitarán los permisos
        }
    }
    
    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                title = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                append(stringResource(R.string.user_greating))
                            }
                            withStyle(style = SpanStyle(color = OrangeDeep)) {
                                append(user?.name ?: "Usuario")
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
            }
        },
        bottomBar = {
            BottomBarUser(navController = navController)
        }
    ) { paddingValues ->
        ContentUser(
            padding = paddingValues,
            navController = navController,
            placesViewModel = placesViewModel,
            user = user,
            onLogout = onLogout
        )
    }
    
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = {
                // No permitir cerrar sin dar permisos la primera vez
                prefs.edit().putBoolean("has_requested_location_permission_$userId", true).apply()
                showPermissionDialog = false
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = OrangeDeep,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Permisos de ubicación",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MontserratFamily
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Para brindarte una mejor experiencia, necesitamos acceso a tu ubicación.",
                        fontSize = 16.sp,
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Esto nos permite:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "• Mostrarte lugares cercanos a tu ubicación",
                        fontSize = 14.sp,
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "• Mejorar las recomendaciones de lugares",
                        fontSize = 14.sp,
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "• Facilitar la búsqueda de lugares",
                        fontSize = 14.sp,
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                        prefs.edit().putBoolean("has_requested_location_permission_$userId", true).apply()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeDeep
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Permitir",
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        prefs.edit().putBoolean("has_requested_location_permission_$userId", true).apply()
                        showPermissionDialog = false
                    }
                ) {
                    Text(
                        text = "Ahora no",
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
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
