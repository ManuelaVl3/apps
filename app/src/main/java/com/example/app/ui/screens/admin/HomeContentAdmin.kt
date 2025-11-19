package com.example.app.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.app.R
import com.example.app.model.PlaceStatus
import com.example.app.ui.screens.admin.nav.RouteTabAdmin
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.OrangeDeep
import com.example.app.viewmodel.PlacesViewModel

@Composable
fun HomeContentAdmin(
    placesViewModel: PlacesViewModel,
    navController: NavHostController = rememberNavController()
) {
    val allPlaces by placesViewModel.places.collectAsState()
    
    // Mostrar solo lugares pendientes y autorizados (excluir rechazados)
    // Ordenar: primero pendientes, luego autorizados, y dentro de cada grupo los más recientes primero
    val placesToShow = remember(allPlaces) {
        val filtered = allPlaces.filter { it.status == PlaceStatus.PENDING || it.status == PlaceStatus.AUTHORIZED }
        val pending = filtered.filter { it.status == PlaceStatus.PENDING }.reversed()
        val authorized = filtered.filter { it.status == PlaceStatus.AUTHORIZED }.reversed()
        pending + authorized
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
            // Saludo
            Text(
                text = buildAnnotatedString {
                    append("Hola, ")
                    withStyle(style = SpanStyle(color = OrangeDeep)) {
                        append("Moderador")
                    }
                    append(" 👋")
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily
            )
            
            Spacer(Modifier.height(24.dp))
            
            // Lista de lugares
            if (placesToShow.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay lugares para revisar",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontFamily = MontserratFamily
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(placesToShow) { place ->
                        PlaceCardAdmin(
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
private fun PlaceCardAdmin(
    place: com.example.app.model.Place,
    navController: NavHostController
) {
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
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen
                AsyncImage(
                    model = if (place.images.firstOrNull() == "place") {
                        R.drawable.place
                    } else {
                        place.images.firstOrNull()
                    },
                    contentDescription = "Imagen del lugar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.place)
                )

                Spacer(Modifier.width(16.dp))

                // Información del lugar
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = place.placeName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )

                    Spacer(Modifier.height(8.dp))

                    // Sistema de estrellas
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < place.rating.toInt())
                                    OrangeDeep
                                else
                                    Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = place.address,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontFamily = MontserratFamily
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = place.phones.firstOrNull() ?: "",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontFamily = MontserratFamily
                    )
                }
            }
            
            // Botón de estado en la esquina inferior derecha
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            ) {
                val (statusText, statusColor) = when (place.status) {
                    PlaceStatus.PENDING -> "Pendiente" to Color(0xFF4CAF50) // Verde
                    PlaceStatus.AUTHORIZED -> "Autorizado" to Color(0xFF5D4037) // Café/marrón
                    PlaceStatus.REJECTED -> "Rechazado" to Color(0xFF5D4037) // Café/marrón
                }
                
                val isClickable = place.status == PlaceStatus.PENDING
                
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor,
                    modifier = if (isClickable) {
                        Modifier.clickable {
                            // Navegar a la página de autorizar cuando es pendiente
                            navController.navigate(RouteTabAdmin.Authorize)
                        }
                    } else {
                        Modifier // No clickeable para lugares autorizados
                    }
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
        }
    }
}

