package com.example.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.app.model.PlaceStatus
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.OrangeDeep
import com.example.app.viewmodel.PlacesViewModel

@Composable
private fun PendingPlaceCard(
    place: com.example.app.model.Place,
    navController: NavHostController,
    onAuthorize: () -> Unit = {},
    onReject: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(com.example.app.ui.screens.admin.nav.RouteTabAdmin.PlaceDetail(place.id))
            },
        colors = CardDefaults.cardColors(
            containerColor = com.example.app.ui.theme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.placeName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = MontserratFamily,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(Modifier.width(16.dp))
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF81C784), 
                    modifier = Modifier.clickable { onAuthorize() }
                ) {
                    Text(
                        text = "Autorizar",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontFamily = MontserratFamily,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF5D4037), 
                    modifier = Modifier.clickable { onReject() }
                ) {
                    Text(
                        text = "Rechazar",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontFamily = MontserratFamily,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorizeAdmin(
    placesViewModel: PlacesViewModel = remember { PlacesViewModel() },
    navController: NavHostController = rememberNavController()
) {
    val allPlaces by placesViewModel.places.collectAsState()
    
    val pendingPlaces = remember(allPlaces) {
        allPlaces.filter { it.status == PlaceStatus.PENDING }
    }
    
    var showRejectDialog by remember { mutableStateOf(false) }
    var selectedPlaceId by remember { mutableStateOf<String?>(null) }
    var rejectionReason by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { snackbarData ->
                        val isAuthorized = snackbarData.visuals.message.contains("autorizado", ignoreCase = true)
                        Snackbar(
                            snackbarData = snackbarData,
                            containerColor = if (isAuthorized) Color(0xFF4CAF50) else Color(0xFF5D4037),
                            contentColor = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Autorizaciones",
                    tint = Color(0xFFE53935), // Rojo
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(Modifier.width(12.dp))
                
                Text(
                    text = "Autorizaciones pendientes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            if (pendingPlaces.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay autorizaciones pendientes",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontFamily = MontserratFamily,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pendingPlaces) { place ->
                        PendingPlaceCard(
                            place = place,
                            navController = navController,
                            onAuthorize = {
                                placesViewModel.updatePlaceStatus(place.id, PlaceStatus.AUTHORIZED)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "El lugar ha sido autorizado",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            onReject = {
                                selectedPlaceId = place.id
                                rejectionReason = ""
                                showRejectDialog = true
                            }
                        )
                    }
                }
            }
            
            if (showRejectDialog && selectedPlaceId != null) {
                AlertDialog(
                    onDismissRequest = {
                        showRejectDialog = false
                        selectedPlaceId = null
                        rejectionReason = ""
                    },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Motivo de rechazo",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = MontserratFamily
                            )
                            IconButton(
                                onClick = {
                                    showRejectDialog = false
                                    selectedPlaceId = null
                                    rejectionReason = ""
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    },
                    text = {
                        OutlinedTextField(
                            value = rejectionReason,
                            onValueChange = { rejectionReason = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = "Explica el motivo del rechazo",
                                    fontFamily = MontserratFamily
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Ej: Información incompleta, datos incorrectos...",
                                    fontFamily = MontserratFamily
                                )
                            },
                            minLines = 4,
                            maxLines = 6,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangeDeep,
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                            )
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (rejectionReason.isNotBlank() && selectedPlaceId != null) {
                                    placesViewModel.updatePlaceStatus(
                                        selectedPlaceId!!,
                                        PlaceStatus.REJECTED,
                                        rejectionReason
                                    )
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "El lugar ha sido rechazado",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    showRejectDialog = false
                                    selectedPlaceId = null
                                    rejectionReason = ""
                                }
                            },
                            enabled = rejectionReason.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OrangeDeep
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Rechazar",
                                fontFamily = MontserratFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showRejectDialog = false
                                selectedPlaceId = null
                                rejectionReason = ""
                            }
                        ) {
                            Text(
                                text = "Cancelar",
                                fontFamily = MontserratFamily,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    shape = RoundedCornerShape(20.dp)
                )
            }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorizeAdminPreview() {
    AppTheme {
        AuthorizeAdmin()
    }
}
