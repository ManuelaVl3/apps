package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.OrangeDeep
import com.example.app.viewmodel.FavoritesViewModel
import com.example.app.viewmodel.PlacesViewModel

@Composable
fun Favorites(
    userId: String = "1",
    favoritesViewModel: FavoritesViewModel,
    placesViewModel: PlacesViewModel,
    onPlaceClick: (String) -> Unit = {}
) {
    val favorites by favoritesViewModel.favorites.collectAsState()
    val allPlaces by placesViewModel.places.collectAsState()
    
    // Obtener los lugares favoritos del usuario (solo autorizados)
    val favoritePlaces = remember(favorites, allPlaces, userId) {
        favorites
            .filter { it.userId == userId }
            .mapNotNull { favorite ->
                allPlaces.find { it.id == favorite.placeId && it.status == com.example.app.model.PlaceStatus.AUTHORIZED }
            }
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
            // Título
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.favorite_24),
                    contentDescription = "Favoritos",
                    colorFilter = ColorFilter.tint(OrangeDeep),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(Modifier.width(12.dp))
                
                Text(
                    text = "Favoritos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Lista de lugares favoritos
            if (favoritePlaces.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tienes lugares favoritos aún",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            fontFamily = MontserratFamily,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Agrega lugares a favoritos desde su detalle",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            fontFamily = MontserratFamily,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Text(
                    text = "Mis lugares favoritos (${favoritePlaces.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
                
                Spacer(Modifier.height(16.dp))
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoritePlaces) { place ->
                        FavoritePlaceCard(
                            place = place,
                            onClick = { onPlaceClick(place.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritePlaceCard(
    place: com.example.app.model.Place,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = com.example.app.ui.theme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen a la izquierda
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
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesPreview() {
    AppTheme {
        Favorites(
            favoritesViewModel = FavoritesViewModel(),
            placesViewModel = PlacesViewModel()
        )
    }
}
