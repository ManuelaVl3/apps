package com.example.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
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
import com.example.app.model.PlaceStatus
import com.example.app.ui.screens.admin.nav.RouteTabAdmin
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.viewmodel.PlacesViewModel

@Composable
fun HistoryAdmin(
    placesViewModel: PlacesViewModel = remember { PlacesViewModel() },
    navController: NavHostController = rememberNavController()
) {
    val allPlaces by placesViewModel.places.collectAsState()
    
    val historyPlaces = remember(allPlaces) {
        placesViewModel.getHistoryPlaces()
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Título con icono
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Historial",
                    tint = com.example.app.ui.theme.OrangeDeep,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(Modifier.width(12.dp))
                
                Text(
                    text = "Historial",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Lista de lugares en el historial
            if (historyPlaces.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay historial de autorizaciones",
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
                    items(historyPlaces) { place ->
                        HistoryPlaceCard(
                            place = place,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryPlaceCard(
    place: com.example.app.model.Place,
    navController: NavHostController
) {
    val (statusText, statusColor) = when (place.status) {
        PlaceStatus.AUTHORIZED -> "Autorizado" to Color(0xFF4CAF50) // Verde
        PlaceStatus.REJECTED -> "Rechazado" to Color(0xFF5D4037) // Café/marrón
        else -> "" to Color.Transparent
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(RouteTabAdmin.PlaceDetail(place.id))
            },
        colors = CardDefaults.cardColors(
            containerColor = com.example.app.ui.theme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor
                ) {
                    Text(
                        text = statusText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontFamily = MontserratFamily,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            if (place.status == PlaceStatus.REJECTED && !place.rejectionReason.isNullOrBlank()) {
                Column {
                    Text(
                        text = "Motivo de rechazo:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontFamily = MontserratFamily
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = place.rejectionReason ?: "",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryAdminPreview() {
    AppTheme {
        HistoryAdmin()
    }
}
