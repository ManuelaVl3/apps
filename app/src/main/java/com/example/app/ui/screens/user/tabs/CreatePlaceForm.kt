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
import androidx.compose.material.icons.filled.ModeEdit
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
import com.example.app.model.PlaceStatus
import com.example.app.model.Schedule
import com.example.app.ui.components.DropdownMenu
import com.example.app.ui.components.ScheduleSection
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.ui.theme.Peach
import com.example.app.viewmodel.PlacesViewModel
import com.example.app.viewmodel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceForm(
    userId: String,
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel,
    placeId: String? = null,
    onBack: () -> Unit = {}
) {
    val isEditMode = placeId != null
    
    var placeName by rememberSaveable { 
        mutableStateOf("") 
    }
    var description by rememberSaveable { 
        mutableStateOf("") 
    }
    var schedules by rememberSaveable { 
        mutableStateOf(listOf<Schedule>()) 
    }
    var phone by rememberSaveable { 
        mutableStateOf("") 
    }
    var city by rememberSaveable { 
        mutableStateOf("") 
    }
    var address by rememberSaveable { 
        mutableStateOf("") 
    }
    var category by rememberSaveable { 
        mutableStateOf("") 
    }
    
    // Estados de edición (solo en modo edición)
    var isEditingName by rememberSaveable { mutableStateOf(!isEditMode) }
    var isEditingDescription by rememberSaveable { mutableStateOf(!isEditMode) }
    var isEditingPhone by rememberSaveable { mutableStateOf(!isEditMode) }
    var isEditingAddress by rememberSaveable { mutableStateOf(!isEditMode) }
    var isEditingCategory by rememberSaveable { mutableStateOf(!isEditMode) }
    var isEditingSchedules by rememberSaveable { mutableStateOf(!isEditMode) }
    
    // Valores originales para comparar cambios (solo en modo edición)
    var originalPlaceName by remember { mutableStateOf("") }
    var originalDescription by remember { mutableStateOf("") }
    var originalSchedules by remember { mutableStateOf(listOf<Schedule>()) }
    var originalPhone by remember { mutableStateOf("") }
    var originalCity by remember { mutableStateOf("") }
    var originalAddress by remember { mutableStateOf("") }
    var originalCategory by remember { mutableStateOf("") }
    
    // Cargar datos del lugar cuando se carga para editar
    LaunchedEffect(placeId) {
        placeId?.let { id ->
            val place = placesViewModel.findByPlaceId(id)
            place?.let {
                placeName = it.placeName
                description = it.description
                schedules = it.schedules
                phone = it.phones.firstOrNull() ?: ""
                city = it.location.locationId
                address = it.address
                category = when (it.type) {
                    com.example.app.model.PlaceType.RESTAURANT -> "Restaurantes"
                    com.example.app.model.PlaceType.FASTFOOD -> "Comidas rápidas"
                    com.example.app.model.PlaceType.COFFEESHOP -> "Cafetería"
                    com.example.app.model.PlaceType.MUSEUM -> "Museos"
                    com.example.app.model.PlaceType.HOTEL -> "Hoteles"
                }
                
                // Guardar valores originales
                originalPlaceName = it.placeName
                originalDescription = it.description
                originalSchedules = it.schedules
                originalPhone = it.phones.firstOrNull() ?: ""
                originalCity = it.location.locationId
                originalAddress = it.address
                originalCategory = when (it.type) {
                    com.example.app.model.PlaceType.RESTAURANT -> "Restaurantes"
                    com.example.app.model.PlaceType.FASTFOOD -> "Comidas rápidas"
                    com.example.app.model.PlaceType.COFFEESHOP -> "Cafetería"
                    com.example.app.model.PlaceType.MUSEUM -> "Museos"
                    com.example.app.model.PlaceType.HOTEL -> "Hoteles"
                }
            }
        }
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val categories = listOf("Restaurantes", "Comidas rápidas", "Cafetería", "Museos", "Hoteles")
    val cities = listOf("Armenia", "Pereira", "Cartagena", "Medellín", "Barranquilla", "Bogotá")
    
    // Verificar si hay cambios en modo edición
    val hasChanges = remember(placeName, description, schedules, phone, city, address, category, 
                              originalPlaceName, originalDescription, originalSchedules, 
                              originalPhone, originalCity, originalAddress, originalCategory) {
        if (!isEditMode) return@remember true // En modo creación, siempre permitir guardar si el formulario es válido
        
        // Comparar cada campo con su valor original
        val nameChanged = placeName != originalPlaceName
        val descriptionChanged = description != originalDescription
        val phoneChanged = phone != originalPhone
        val cityChanged = city != originalCity
        val addressChanged = address != originalAddress
        val categoryChanged = category != originalCategory
        val schedulesChanged = schedules != originalSchedules
        
        nameChanged || descriptionChanged || phoneChanged || cityChanged || addressChanged || categoryChanged || schedulesChanged
    }
    
    val isFormValid = remember(placeName, description, schedules, phone, city, address, category) {
        val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        
        val valid = placeName.isNotBlank() && description.isNotBlank() && 
                     schedules.isNotEmpty() && phone.isNotBlank() && 
                     city.isNotBlank() && address.isNotBlank() && category.isNotBlank() &&
                     schedules.all { schedule ->
                         val openDayIndex = daysOfWeek.indexOf(schedule.openDay)
                         val closeDayIndex = daysOfWeek.indexOf(schedule.closeDay)
                         
                         val isScheduleComplete = schedule.openDay.isNotBlank() && 
                                                 schedule.openTime.isNotBlank() && 
                                                 schedule.closeDay.isNotBlank() &&
                                                 schedule.closeTime.isNotBlank()
                         
                         val isScheduleValid = if (openDayIndex == closeDayIndex) {
                             // Mismo día: hora de cierre debe ser mayor
                             schedule.openTime < schedule.closeTime
                         } else {
                             // Días diferentes: día de cierre debe ser posterior
                             openDayIndex < closeDayIndex
                         }
                         
                         isScheduleComplete && isScheduleValid
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
            val openDayIndex = daysOfWeek.indexOf(schedule.openDay)
            val closeDayIndex = daysOfWeek.indexOf(schedule.closeDay)
            val isScheduleComplete = schedule.openDay.isNotBlank() && 
                                     schedule.openTime.isNotBlank() && 
                                     schedule.closeDay.isNotBlank() &&
                                     schedule.closeTime.isNotBlank()
            val isScheduleValid = if (openDayIndex == closeDayIndex) {
                schedule.openTime < schedule.closeTime
            } else {
                openDayIndex < closeDayIndex
            }
            println("  Schedule $index: ${schedule.openDay} ${schedule.openTime} - ${schedule.closeDay} ${schedule.closeTime} -> complete=$isScheduleComplete, valid=$isScheduleValid")
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
                        focusedBorderColor = if (isEditingName) OrangeDeep else Orange,
                        unfocusedBorderColor = if (isEditingName) OrangeDeep else Orange.copy(alpha = 0.6f)
                    ),
                    enabled = isEditingName || !isEditMode,
                    trailingIcon = if (isEditMode) {
                        {
                            IconButton(
                                onClick = { isEditingName = !isEditingName }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ModeEdit,
                                    contentDescription = if (isEditingName) "Guardar nombre" else "Editar nombre",
                                    tint = Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null
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
                        focusedBorderColor = if (isEditingDescription) OrangeDeep else Orange,
                        unfocusedBorderColor = if (isEditingDescription) OrangeDeep else Orange.copy(alpha = 0.6f)
                    ),
                    enabled = isEditingDescription || !isEditMode,
                    maxLines = 4,
                    trailingIcon = if (isEditMode) {
                        {
                            IconButton(
                                onClick = { isEditingDescription = !isEditingDescription }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ModeEdit,
                                    contentDescription = if (isEditingDescription) "Guardar descripción" else "Editar descripción",
                                    tint = Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null
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
                        focusedBorderColor = if (isEditingPhone) OrangeDeep else Orange,
                        unfocusedBorderColor = if (isEditingPhone) OrangeDeep else Orange.copy(alpha = 0.6f)
                    ),
                    enabled = isEditingPhone || !isEditMode,
                    trailingIcon = if (isEditMode) {
                        {
                            IconButton(
                                onClick = { isEditingPhone = !isEditingPhone }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ModeEdit,
                                    contentDescription = if (isEditingPhone) "Guardar teléfono" else "Editar teléfono",
                                    tint = Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null
                )
                
                // Ciudad
                DropdownMenu(
                    options = cities,
                    value = city,
                    onValueChange = { city = it },
                    label = stringResource(R.string.city),
                    enabled = isEditingCategory || !isEditMode,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = if (isEditMode) {
                        {
                            IconButton(
                                onClick = { isEditingCategory = !isEditingCategory }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ModeEdit,
                                    contentDescription = if (isEditingCategory) "Guardar ciudad" else "Editar ciudad",
                                    tint = Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null
                )
                
                // Dirección
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.create_address)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isEditingAddress) OrangeDeep else Orange,
                        unfocusedBorderColor = if (isEditingAddress) OrangeDeep else Orange.copy(alpha = 0.6f)
                    ),
                    enabled = isEditingAddress || !isEditMode,
                    trailingIcon = if (isEditMode) {
                        {
                            IconButton(
                                onClick = { isEditingAddress = !isEditingAddress }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ModeEdit,
                                    contentDescription = if (isEditingAddress) "Guardar dirección" else "Editar dirección",
                                    tint = Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null
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
                    enabled = isEditingCategory || !isEditMode,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = if (isEditMode) {
                        {
                            IconButton(
                                onClick = { isEditingCategory = !isEditingCategory }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ModeEdit,
                                    contentDescription = if (isEditingCategory) "Guardar categoría" else "Editar categoría",
                                    tint = Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else null
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Botón Guardar
                Button(
                    onClick = {
                        val placeType = when (category) {
                            "Restaurantes" -> com.example.app.model.PlaceType.RESTAURANT
                            "Comidas rápidas" -> com.example.app.model.PlaceType.FASTFOOD
                            "Cafetería" -> com.example.app.model.PlaceType.COFFEESHOP
                            "Museos" -> com.example.app.model.PlaceType.MUSEUM
                            "Hoteles" -> com.example.app.model.PlaceType.HOTEL
                            else -> com.example.app.model.PlaceType.RESTAURANT
                        }
                        
                        // Mapear ciudad a ubicación
                        val location = when (city) {
                            "Armenia" -> com.example.app.model.Location("loc1", 4.5339, -75.6811)
                            "Pereira" -> com.example.app.model.Location("loc2", 4.8133, -75.6961)
                            "Cartagena" -> com.example.app.model.Location("loc3", 10.3910, -75.4794)
                            "Medellín" -> com.example.app.model.Location("loc4", 6.2476, -75.5658)
                            "Barranquilla" -> com.example.app.model.Location("loc5", 10.9639, -74.7964)
                            "Bogotá" -> com.example.app.model.Location("loc6", 4.7110, -74.0721)
                            else -> com.example.app.model.Location("loc1", 4.5339, -75.6811) // Armenia por defecto
                        }
                        
                        if (isEditMode && placeId != null) {
                            // Modo edición
                            val existingPlace = placesViewModel.findByPlaceId(placeId)
                            existingPlace?.let { place ->
                                val updatedPlace = place.copy(
                                    placeName = placeName,
                                    description = description,
                                    phones = listOf(phone),
                                    type = placeType,
                                    schedules = schedules,
                                    location = location,
                                    address = address
                                )
                                placesViewModel.update(placeId, updatedPlace)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Lugar actualizado exitosamente")
                                }
                                onBack()
                            }
                        } else {
                                   val newPlace = com.example.app.model.Place(
                                       id = java.util.UUID.randomUUID().toString(),
                                       images = listOf("place"),
                                       placeName = placeName,
                                       description = description,
                                       phones = listOf(phone),
                                       type = placeType,
                                       schedules = schedules,
                                       location = location,
                                       address = address,
                                       createBy = userId,
                                       status = PlaceStatus.PENDING
                                   )
                            placesViewModel.create(newPlace)
                            scope.launch {
                                snackbarHostState.showSnackbar("Lugar creado exitosamente")
                            }
                            onBack()
                        }
                    },
                    enabled = if (isEditMode) {
                        isFormValid && hasChanges
                    } else {
                        isFormValid
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (if (isEditMode) (isFormValid && hasChanges) else isFormValid) OrangeDeep else Peach,
                        contentColor = Color.White,
                        disabledContainerColor = Peach,
                        disabledContentColor = Color.White
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
                modifier = Modifier.align(Alignment.BottomCenter),
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = Orange,
                        contentColor = Color.White,
                        actionColor = Color.White
                    )
                }
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
