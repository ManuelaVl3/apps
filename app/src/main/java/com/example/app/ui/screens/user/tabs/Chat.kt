package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.R
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.viewmodel.MessagesViewModel
import com.example.app.viewmodel.PlacesViewModel
import com.example.app.viewmodel.UsersViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(
    friendId: String,
    userId: String = "1",
    messagesViewModel: MessagesViewModel,
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel = remember { UsersViewModel() },
    onBack: () -> Unit = {},
    onPlaceClick: (String) -> Unit = {}
) {
    val friend = usersViewModel.findByUserId(friendId)
    var messageText by remember { mutableStateOf("") }
    var showShareDialog by remember { mutableStateOf(false) }
    var showSharedPlacesHistory by remember { mutableStateOf(false) }
    
    // Observar los mensajes en tiempo real
    val allMessages by messagesViewModel.messages.collectAsState()
    val messages = remember(allMessages, userId, friendId) {
        allMessages.filter { message ->
            (message.senderId == userId && message.receiverId == friendId) ||
            (message.senderId == friendId && message.receiverId == userId)
        }.sortedBy { it.timestamp }
    }
    
    // Obtener lugares compartidos (mensajes con placeId)
    val sharedPlaces = remember(allMessages, userId, friendId) {
        allMessages.filter { message ->
            ((message.senderId == userId && message.receiverId == friendId) ||
             (message.senderId == friendId && message.receiverId == userId)) &&
            message.placeId != null && message.placeId.isNotBlank()
        }.sortedByDescending { it.timestamp }
    }
    
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // Scroll al final cuando hay nuevos mensajes
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            kotlinx.coroutines.delay(50)
            scope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = friend?.name ?: "Amigo",
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
                },
                actions = {
                    IconButton(
                        onClick = { showSharedPlacesHistory = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Historial de lugares compartidos",
                            tint = OrangeDeep
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Lista de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(
                        message = message,
                        isFromMe = message.senderId == userId,
                        placesViewModel = placesViewModel,
                        onPlaceClick = onPlaceClick
                    )
                }
            }
            
            // Campo de entrada de mensaje
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Escribe un mensaje...",
                            fontFamily = MontserratFamily,
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
                
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messagesViewModel.sendMessage(userId, friendId, messageText.trim())
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = OrangeDeep,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
    
    // Diálogo para compartir lugares
    if (showShareDialog) {
        SharePlaceDialog(
            userId = userId,
            friendId = friendId,
            placesViewModel = placesViewModel,
            messagesViewModel = messagesViewModel,
            onDismiss = { showShareDialog = false }
        )
    }
    
    // Diálogo para mostrar historial de lugares compartidos
    if (showSharedPlacesHistory) {
        SharedPlacesHistoryDialog(
            sharedPlaces = sharedPlaces,
            placesViewModel = placesViewModel,
            userId = userId,
            friendId = friendId,
            usersViewModel = usersViewModel,
            onDismiss = { showSharedPlacesHistory = false },
            onPlaceClick = onPlaceClick
        )
    }
}

@Composable
private fun MessageBubble(
    message: com.example.app.model.Message,
    isFromMe: Boolean,
    placesViewModel: PlacesViewModel,
    onPlaceClick: (String) -> Unit = {}
) {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = dateFormat.format(Date(message.timestamp))
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
        ) {
            // Si el mensaje tiene un lugar compartido, mostrarlo
            if (message.placeId != null && message.placeId.isNotBlank()) {
                val place = placesViewModel.findByPlaceId(message.placeId)
                if (place != null) {
                    SharedPlaceCard(
                        place = place,
                        isFromMe = isFromMe,
                        onClick = { onPlaceClick(place.id) }
                    )
                } else {
                    // Si el lugar no se encuentra, mostrar un mensaje indicando que se compartió un lugar
                    Text(
                        text = "📍 Lugar compartido: ${message.content}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontFamily = MontserratFamily,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            
            // Burbuja de mensaje
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isFromMe) 16.dp else 4.dp,
                    bottomEnd = if (isFromMe) 4.dp else 16.dp
                ),
                color = if (isFromMe) OrangeDeep else Color(0xFFE0E0E0),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = message.content,
                        fontSize = 14.sp,
                        color = if (isFromMe) Color.White else MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = timeString,
                        fontSize = 10.sp,
                        color = if (isFromMe) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontFamily = MontserratFamily
                    )
                }
            }
        }
    }
}

@Composable
private fun SharedPlaceCard(
    place: com.example.app.model.Place,
    isFromMe: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFromMe) Orange.copy(alpha = 0.2f) else Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = if (place.images.firstOrNull() == "place") {
                    R.drawable.place
                } else {
                    place.images.firstOrNull()
                },
                contentDescription = "Imagen del lugar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.place)
            )
            
            Spacer(Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = OrangeDeep,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = place.placeName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )
                }
                
                Spacer(Modifier.height(4.dp))
                
                Text(
                    text = place.address,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontFamily = MontserratFamily,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun SharePlaceDialog(
    userId: String,
    friendId: String,
    placesViewModel: PlacesViewModel,
    messagesViewModel: MessagesViewModel,
    onDismiss: () -> Unit
) {
    val allPlaces by placesViewModel.places.collectAsState()
    val userPlaces = placesViewModel.findByCreator(userId)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Compartir lugar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily
            )
        },
        text = {
            if (userPlaces.isEmpty()) {
                Text(
                    text = "No tienes lugares para compartir",
                    fontFamily = MontserratFamily
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(userPlaces) { place ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    messagesViewModel.sharePlace(userId, friendId, place.id, place.placeName)
                                    onDismiss()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = com.example.app.ui.theme.CardBackground
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = if (place.images.firstOrNull() == "place") {
                                        R.drawable.place
                                    } else {
                                        place.images.firstOrNull()
                                    },
                                    contentDescription = "Imagen del lugar",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(id = R.drawable.place)
                                )
                                
                                Spacer(Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = place.placeName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontFamily = MontserratFamily
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = place.address,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                        fontFamily = MontserratFamily
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    fontFamily = MontserratFamily
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun SharedPlacesHistoryDialog(
    sharedPlaces: List<com.example.app.model.Message>,
    placesViewModel: PlacesViewModel,
    userId: String,
    friendId: String,
    usersViewModel: UsersViewModel = remember { UsersViewModel() },
    onDismiss: () -> Unit,
    onPlaceClick: (String) -> Unit
) {
    val friend = usersViewModel.findByUserId(friendId)
    val currentUser = usersViewModel.findByUserId(userId)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Lugares compartidos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily
            )
        },
        text = {
            if (sharedPlaces.isEmpty()) {
                Text(
                    text = "No hay lugares compartidos aún",
                    fontFamily = MontserratFamily
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 500.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sharedPlaces) { message ->
                        val place = message.placeId?.let { placesViewModel.findByPlaceId(it) }
                        if (place != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onPlaceClick(place.id)
                                        onDismiss()
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFF5F3)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = if (place.images.firstOrNull() == "place") {
                                            R.drawable.place
                                        } else {
                                            place.images.firstOrNull()
                                        },
                                        contentDescription = "Imagen del lugar",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop,
                                        error = painterResource(id = R.drawable.place)
                                    )
                                    
                                    Spacer(Modifier.width(12.dp))
                                    
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = OrangeDeep,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text = place.placeName,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontFamily = MontserratFamily
                                            )
                                        }
                                        
                                        Spacer(Modifier.height(4.dp))
                                        
                                        Text(
                                            text = place.address,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                            fontFamily = MontserratFamily,
                                            maxLines = 1
                                        )
                                        
                                        Spacer(Modifier.height(4.dp))
                                        
                                        Text(
                                            text = if (message.senderId == userId) 
                                                "Compartido por ti" 
                                            else 
                                                "Compartido por ${friend?.name ?: "amigo"}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                            fontFamily = MontserratFamily
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cerrar",
                    fontFamily = MontserratFamily
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

