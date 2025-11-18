package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search() {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Categoría") }
    var showCategoryMenu by remember { mutableStateOf(false) }
    
    val categories = listOf("Restaurantes", "Comidas rápidas", "Cafetería", "Museos", "Hoteles")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            if (isSearchActive) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { 
                            isSearchActive = false
                            showCategoryMenu = false
                            selectedCategory = "Categoría"
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
                                text = "Buscar por nombre del lugar",
                                fontFamily = MontserratFamily,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            ) 
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
                
                Spacer(Modifier.height(24.dp))
                
                Text(
                    text = "Filtrar por:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
                
                Spacer(Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterChip(
                        selected = showCategoryMenu,
                        onClick = { showCategoryMenu = !showCategoryMenu },
                        label = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = selectedCategory,
                                    fontFamily = MontserratFamily,
                                    fontSize = 14.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange.copy(alpha = 0.3f),
                            containerColor = Color(0xFFFFF5F3),
                            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
                            labelColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = showCategoryMenu,
                            borderColor = Orange.copy(alpha = 0.3f),
                            selectedBorderColor = Orange
                        )
                    )
                    
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { 
                            Text(
                                text = "Distancia",
                                fontFamily = MontserratFamily,
                                fontSize = 14.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFFFF5F3),
                            labelColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = Orange.copy(alpha = 0.3f)
                        )
                    )
                    
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Km",
                                    fontFamily = MontserratFamily,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "2",
                                    fontFamily = MontserratFamily,
                                    fontSize = 10.sp,
                                    modifier = Modifier.offset(y = (-4).dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFFFF5F3),
                            labelColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = Orange.copy(alpha = 0.3f)
                        )
                    )
                }
                
                if (showCategoryMenu) {
                    Spacer(Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFD3C6)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            categories.forEach { category ->
                                Text(
                                    text = category,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = MontserratFamily,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { 
                                            selectedCategory = category
                                            showCategoryMenu = false
                                        }
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                
            } else {
                Text(
                    text = "Hola, Manuela 👋",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
                
                Spacer(Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        isSearchActive = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { 
                        Text(
                            text = "Buscar por nombre del lugar",
                            fontFamily = MontserratFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        ) 
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
                
                Spacer(Modifier.height(24.dp))
                
                Text(
                    text = "Filtrar por:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
                
                Spacer(Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterChip(
                        selected = false,
                        onClick = { isSearchActive = true },
                        label = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Categoría",
                                    fontFamily = MontserratFamily,
                                    fontSize = 14.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFFFF5F3),
                            labelColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = Orange.copy(alpha = 0.3f)
                        )
                    )
                    
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { 
                            Text(
                                text = "Distancia",
                                fontFamily = MontserratFamily,
                                fontSize = 14.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFFFF5F3),
                            labelColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = Orange.copy(alpha = 0.3f)
                        )
                    )
                    
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Km",
                                    fontFamily = MontserratFamily,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "2",
                                    fontFamily = MontserratFamily,
                                    fontSize = 10.sp,
                                    modifier = Modifier.offset(y = (-4).dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFFFF5F3),
                            labelColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = Orange.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchPreview() {
    AppTheme {
        Search()
    }
}

