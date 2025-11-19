package com.example.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import com.example.app.model.PlaceStatus
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.viewmodel.PlacesViewModel

@Composable
fun AuthorizeAdmin(
    placesViewModel: PlacesViewModel = remember { PlacesViewModel() }
) {
    val allPlaces by placesViewModel.places.collectAsState()
    
    // Filtrar solo lugares pendientes
    val pendingPlaces = remember(allPlaces) {
        allPlaces.filter { it.status == PlaceStatus.PENDING }
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
            
            // Lista de lugares pendientes
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
                            onAuthorize = {
                                placesViewModel.updatePlaceStatus(place.id, PlaceStatus.AUTHORIZED)
                            },
                            onReject = {
                                placesViewModel.updatePlaceStatus(place.id, PlaceStatus.REJECTED)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PendingPlaceCard(
    place: com.example.app.model.Place,
    onAuthorize: () -> Unit = {},
    onReject: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            // Nombre del lugar
            Text(
                text = place.placeName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = MontserratFamily,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(Modifier.width(16.dp))
            
            // Botones de acción
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Autorizar (verde)
                Button(
                    onClick = onAuthorize,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF81C784) // Verde claro
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(
                        text = "Autorizar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontFamily = MontserratFamily
                    )
                }
                
                // Botón Rechazar (café/marrón)
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D4037) // Café/marrón
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(
                        text = "Rechazar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontFamily = MontserratFamily
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorizeAdminPreview() {
    AppTheme {
        AuthorizeAdmin()
    }
}
