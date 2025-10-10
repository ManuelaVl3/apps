package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.R
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.CardBackground
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.OrangeDeep
import com.example.app.viewmodel.PlacesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetail(
    placeId: String,
    placesViewModel: PlacesViewModel,
    onBack: () -> Unit = {}
) {
    val place = placesViewModel.findByPlaceId(placeId)
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (place == null) {
            // Mostrar mensaje si no se encuentra el lugar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Lugar no encontrado",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // TopAppBar
                TopAppBar(
                    title = {
                        Text(
                            text = place.placeName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MontserratFamily
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
                
                // Contenido scrolleable
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(Modifier.height(8.dp))
                    }
                    
                    // Imagen del lugar
                    item {
                        AsyncImage(
                            model = if (place.images.firstOrNull() == "place") {
                                R.drawable.place
                            } else {
                                place.images.firstOrNull()
                            },
                            contentDescription = "Imagen del lugar",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.place)
                        )
                    }
                    
                    // Rating
                    item {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < place.rating.toInt())
                                        OrangeDeep
                                    else
                                        Color.Gray,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    
                    // Descripción
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = CardBackground
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Descripción",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = place.description,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily
                                )
                            }
                        }
                    }
                    
                    // Dirección
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = CardBackground
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = OrangeDeep,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = place.address,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily
                                )
                            }
                        }
                    }
                    
                    // Teléfonos
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = CardBackground
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Contacto",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily
                                )
                                Spacer(Modifier.height(8.dp))
                                place.phones.forEach { phone ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            tint = OrangeDeep,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = phone,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontFamily = MontserratFamily
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                    
                    // Horarios
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = CardBackground
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Horarios",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily
                                )
                                Spacer(Modifier.height(8.dp))
                                place.schedules.forEach { schedule ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = schedule.day,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontFamily = MontserratFamily,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${schedule.open} - ${schedule.close}",
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontFamily = MontserratFamily
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                    
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailPreview() {
    AppTheme {
        PlaceDetail(
            placeId = "1",
            placesViewModel = PlacesViewModel()
        )
    }
}

