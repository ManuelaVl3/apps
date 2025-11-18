package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange

@Composable
fun Map() {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    
    val categories = listOf("Restaurantes", "Comidas rápidas", "Cafetería", "Museos", "Hoteles")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isSearching = true },
                enabled = !isSearching,
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
            
            if (isSearching) {
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
                        color = Color(0xFFFFF5F3),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, 
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
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFFFF5F3),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, 
                            Orange.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = "Distancia",
                            fontFamily = MontserratFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFFFF5F3),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, 
                            Orange.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Km ",
                                fontFamily = MontserratFamily,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "2",
                                fontFamily = MontserratFamily,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.offset(y = (-3).dp)
                            )
                        }
                    }
                }
                
                if (showCategoryMenu) {
                    Spacer(Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFD3C6)
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
                
                return@Column
            }

            Spacer(Modifier.height(45.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                CategoryCard(
                    imageRes = R.drawable.frame_1,
                    modifier = Modifier.weight(1f),
                    borderColor = Color(0xFFFF5722)
                )
                CategoryCard(
                    imageRes = R.drawable.frame_9,
                    modifier = Modifier.weight(1f),
                    borderColor = Color(0xFFFFC107)
                )
                CategoryCard(
                    imageRes = R.drawable.frame_11,
                    modifier = Modifier.weight(1f),
                    borderColor = Color(0xFF2196F3)
                )
            }

            Spacer(Modifier.height(45.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                LocationCard(
                    imageRes = R.drawable.recurso_1,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFE3F2FD)
                )
                LocationCard(
                    imageRes = R.drawable.image_12,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFF1F8E9)
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

@Composable
private fun CategoryCard(
    imageRes: Int,
    modifier: Modifier = Modifier,
    borderColor: Color = Orange
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = modifier
            .size(80.dp)
            .clickable { },
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun LocationCard(
    imageRes: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE3F2FD)
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { },
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
