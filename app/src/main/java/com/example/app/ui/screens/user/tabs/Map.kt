package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.model.PlaceType
import com.example.app.ui.screens.user.tabs.PlaceCard
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.viewmodel.PlacesViewModel

@Composable
fun Map(
    placesViewModel: PlacesViewModel = remember { PlacesViewModel() },
    onPlaceClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var showDistanceMenu by remember { mutableStateOf(false) }
    var selectedDistance by remember { mutableStateOf("") }
    
    val categories = listOf("Restaurantes", "Comidas rápidas", "Cafetería", "Museos", "Hoteles")
    val distances = (1..10).map { "$it km" }
    
    val allPlaces by placesViewModel.places.collectAsState()
    
    val selectedPlaceType = when (selectedCategory) {
        "Restaurantes" -> PlaceType.RESTAURANT
        "Comidas rápidas" -> PlaceType.FASTFOOD
        "Cafetería" -> PlaceType.COFFEESHOP
        "Museos" -> PlaceType.MUSEUM
        "Hoteles" -> PlaceType.HOTEL
        else -> null
    }
    
    // Ubicación de referencia (Armenia, Quindío - centro de la ciudad) TEMPORAL
    val referenceLocation = remember { 
        com.example.app.model.Location("ref", 4.5339, -75.6811) 
    }
    
    val filteredPlaces = remember(searchQuery, selectedPlaceType, selectedDistance) {
        var results = allPlaces
        
        if (searchQuery.isNotBlank()) {
            results = placesViewModel.findByName(searchQuery)
        }
        
        if (selectedPlaceType != null) {
            results = results.filter { it.type == selectedPlaceType }
        }
        
        if (selectedDistance.isNotEmpty()) {
            val distanceKm = selectedDistance.replace(" km", "").toIntOrNull() ?: 0
            results = results.filter { place ->
                val distance = placesViewModel.calculateDistance(
                    referenceLocation.latitude, referenceLocation.longitude,
                    place.location.latitude, place.location.longitude
                )
                distance <= distanceKm
            }
        }
        
        results
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isSearching) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { 
                        isSearching = false
                        searchQuery = ""
                        selectedCategory = ""
                        selectedDistance = ""
                        showCategoryMenu = false
                        showDistanceMenu = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                placeholder = { 
                    Text(
                            text = "Buscar lugares...",
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ) 
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar búsqueda",
                                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                },
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange.copy(alpha = 0.3f),
                    unfocusedBorderColor = Orange.copy(alpha = 0.2f),
                    focusedContainerColor = Color(0xFFFFF5F3),
                        unfocusedContainerColor = Color(0xFFFFF5F3)
                ),
                singleLine = true
            )
            }
            
                Spacer(Modifier.height(20.dp))
                
                Text(
                    text = "Filtrar por:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
                
                Spacer(Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.clickable { showCategoryMenu = !showCategoryMenu },
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, 
                            if (selectedCategory.isNotEmpty())
                                Orange
                            else
                                Orange.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (selectedCategory.isEmpty()) "Categoría" else selectedCategory,
                                fontFamily = MontserratFamily,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        if (selectedCategory.isNotEmpty()) {
                            IconButton(
                                onClick = { 
                                    selectedCategory = ""
                                    showCategoryMenu = false
                                },
                                modifier = Modifier.size(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar categoría",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    }
                    
                    Surface(
                        modifier = Modifier.clickable { showDistanceMenu = !showDistanceMenu },
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, 
                            if (selectedDistance.isNotEmpty())
                                Orange
                            else
                                Orange.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                            text = if (selectedDistance.isEmpty()) "Distancia" else selectedDistance,
                                fontFamily = MontserratFamily,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        if (selectedDistance.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar distancia",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { 
                                        selectedDistance = ""
                                        showDistanceMenu = false
                                    }
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        }
                    }
                }
                
                if (showCategoryMenu) {
                    Spacer(Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Orange.copy(alpha = 0.3f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { category ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedCategory = category
                                            showCategoryMenu = false
                                        },
                                    color = if (selectedCategory == category) 
                                        Color(0xFFFFBFAD) 
                                    else 
                                        Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = category,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontFamily = MontserratFamily,
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
            if (showDistanceMenu) {
                Spacer(Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Orange.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        distances.forEach { distance ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedDistance = distance
                                        showDistanceMenu = false
                                    },
                                color = if (selectedDistance == distance) 
                                    Color(0xFFFFBFAD) 
                                else 
                                    Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = distance,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            if (filteredPlaces.isEmpty()) {
                Spacer(Modifier.height(100.dp))
                Text(
                    text = if (searchQuery.isBlank() && selectedCategory.isEmpty() && selectedDistance.isEmpty()) 
                        "Escribe algo para buscar o selecciona una categoría o distancia"
                    else 
                        "No se encontraron lugares",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontFamily = MontserratFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPlaces) { place ->
                        PlaceCard(
                            place = place,
                            onClick = { onPlaceClick(place.id) }
                        )
                    }
                }
            }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    if (it.isNotBlank()) {
                        isSearching = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isSearching = true },
                enabled = true,
                placeholder = { 
                    Text(
                        text = "¿De qué tienes antojo?",
                        fontFamily = MontserratFamily,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ) 
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                },
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange.copy(alpha = 0.3f),
                    unfocusedBorderColor = Orange.copy(alpha = 0.2f),
                    focusedContainerColor = Color(0xFFFFF5F3),
                    unfocusedContainerColor = Color(0xFFFFF5F3),
                    disabledContainerColor = Color(0xFFFFF5F3),
                    disabledBorderColor = Orange.copy(alpha = 0.2f)
                ),
                singleLine = true
            )

            Spacer(Modifier.height(45.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                CategoryCard(
                    imageRes = R.drawable.fast_food,
                    modifier = Modifier.weight(1f),
                    borderColor = Color(0xFFFF5722),
                    onClick = {
                        selectedCategory = "Comidas rápidas"
                        isSearching = true
                    }
                )
                CategoryCard(
                    imageRes = R.drawable.restaurant,
                    modifier = Modifier.weight(1f),
                    borderColor = Color(0xFFFFC107),
                    onClick = {
                        selectedCategory = "Restaurantes"
                        isSearching = true
                    }
                )
                CategoryCard(
                    imageRes = R.drawable.coffe_shop,
                    modifier = Modifier.weight(1f),
                    borderColor = Color(0xFF2196F3),
                    onClick = {
                        selectedCategory = "Cafetería"
                        isSearching = true
                    }
                )
            }

            Spacer(Modifier.height(45.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                LocationCard(
                    imageRes = R.drawable.hotel,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFE3F2FD),
                    onClick = {
                        selectedCategory = "Hoteles"
                        isSearching = true
                    }
                )
                LocationCard(
                    imageRes = R.drawable.museum,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFF1F8E9),
                    onClick = {
                        selectedCategory = "Museos"
                        isSearching = true
                    }
                )
            }

            Spacer(Modifier.height(32.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFFFEFEB)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Échale un ojo a tus sitios cercanos",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = MontserratFamily
            )

            Spacer(Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "Mapa",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CategoryCard(
    imageRes: Int,
    modifier: Modifier = Modifier,
    borderColor: Color = Orange,
    onClick: () -> Unit = {}
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = modifier
            .size(80.dp)
            .clickable { onClick() },
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun LocationCard(
    imageRes: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE3F2FD),
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapPreview() {
    AppTheme {
        Map()
    }
}
