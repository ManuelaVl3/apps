package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.model.ScheduleData
import com.example.app.ui.components.DropdownMenu
import com.example.app.ui.components.ScheduleSection
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.Peach
import com.example.app.viewmodel.PlacesViewModel
import com.example.app.viewmodel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceForm(
    userId: String,
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel,
    onBack: () -> Unit = {}
) {
    var placeName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var schedules by rememberSaveable { mutableStateOf(listOf<ScheduleData>()) }
    var phone by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val categories = listOf("Restaurantes", "Comidas rápidas", "Cafetería", "Museos", "Hoteles")
    val cities = listOf("Armenia", "Pereira", "Cartagena", "Medellín", "Barranquilla", "Bogotá")
    
    val isFormValid = remember(placeName, description, schedules, phone, city, address, category) {
        val valid = placeName.isNotBlank() && description.isNotBlank() && 
                     schedules.isNotEmpty() && phone.isNotBlank() && 
                     city.isNotBlank() && address.isNotBlank() && category.isNotBlank() &&
                     schedules.all { schedule ->
                         schedule.day.isNotBlank() && 
                         schedule.openTime.isNotBlank() && 
                         schedule.closeTime.isNotBlank() &&
                         schedule.openTime < schedule.closeTime
                     }
        // Debug logs
        println("=== VALIDACIÓN FORMULARIO ===")
        println("placeName: '$placeName' -> ${placeName.isNotBlank()}")
        println("description: '$description' -> ${description.isNotBlank()}")
        println("phone: '$phone' -> ${phone.isNotBlank()}")
        println("city: '$city' -> ${city.isNotBlank()}")
        println("address: '$address' -> ${address.isNotBlank()}")
        println("category: '$category' -> ${category.isNotBlank()}")
        println("schedules.size: ${schedules.size}")
        schedules.forEachIndexed { index, schedule ->
            val scheduleValid = schedule.day.isNotBlank() && 
                               schedule.openTime.isNotBlank() && 
                               schedule.closeTime.isNotBlank() &&
                               schedule.openTime < schedule.closeTime
            println("  Schedule $index: day='${schedule.day}' open='${schedule.openTime}' close='${schedule.closeTime}' -> valid=$scheduleValid")
        }
        println("isFormValid: $valid")
        println("===========================")
        valid
    }
                     // Los campos de mapa e imagen son opcionales

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TopAppBar
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Orange,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.create_form_title),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MontserratFamily
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
            
            // Contenido scrolleable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                
                // Nombre del lugar
                OutlinedTextField(
                    value = placeName,
                    onValueChange = { placeName = it },
                    label = { Text(stringResource(R.string.create_place_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                    )
                )
                
                // Descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.create_description)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                    ),
                    maxLines = 4
                )
                
                // Sección de horarios
                ScheduleSection(
                    schedules = schedules,
                    onSchedulesChange = { schedules = it }
                )
                
                // Teléfono de contacto
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(stringResource(R.string.create_contact_phone)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                    )
                )
                
                // Ciudad
                DropdownMenu(
                    options = cities,
                    value = city,
                    onValueChange = { city = it },
                    label = stringResource(R.string.city),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Dirección
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.create_address)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange,
                        unfocusedBorderColor = Orange.copy(alpha = 0.6f)
                    )
                )
                
                // Sección de imagen (opcional)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.create_add_image),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = MontserratFamily
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "(Opcional)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            fontFamily = MontserratFamily
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Orange.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Toca para agregar imagen",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontFamily = MontserratFamily
                            )
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Agregar imagen",
                                tint = Orange.copy(alpha = 0.6f),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
                
                // Sección del mapa (opcional)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ubica en el mapa el lugar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = MontserratFamily
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "(Opcional)",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            fontFamily = MontserratFamily
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Orange.copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Toca para seleccionar ubicación",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontFamily = MontserratFamily
                            )
                            Spacer(Modifier.height(8.dp))
                            // Imagen del mapa
                            Image(
                                painter = painterResource(id = R.drawable.map),
                                contentDescription = "Mapa",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                
                // Categoría
                DropdownMenu(
                    options = categories,
                    value = category,
                    onValueChange = { category = it },
                    label = stringResource(R.string.create_category),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Botón Guardar
                Button(
                    onClick = {
                        // Aquí irá la lógica para guardar el lugar
                        scope.launch {
                            snackbarHostState.showSnackbar("Lugar creado exitosamente")
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        disabledContainerColor = Peach,
                        contentColor = Color.White,
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.create_save),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = MontserratFamily
                    )
                }
                
                Spacer(Modifier.height(24.dp))
            }
        }
        
        // Snackbar
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreatePlaceFormPreview() {
    AppTheme {
        CreatePlaceForm(
            userId = "1",
            placesViewModel = PlacesViewModel(),
            usersViewModel = UsersViewModel()
        )
    }
}
