package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
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
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.viewmodel.PlacesViewModel
import com.example.app.viewmodel.ReviewsViewModel
import com.example.app.viewmodel.FriendsViewModel
import com.example.app.viewmodel.MessagesViewModel
import com.example.app.viewmodel.UsersViewModel
import com.example.app.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetail(
    placeId: String,
    placesViewModel: PlacesViewModel,
    reviewsViewModel: ReviewsViewModel = remember { ReviewsViewModel() },
    friendsViewModel: FriendsViewModel = remember { FriendsViewModel() },
    messagesViewModel: MessagesViewModel,
    favoritesViewModel: FavoritesViewModel = remember { FavoritesViewModel() },
    usersViewModel: UsersViewModel = remember { UsersViewModel() },
    userId: String = "1",
    userRole: com.example.app.model.Role? = null,
    onBack: () -> Unit = {},
    onEdit: (String) -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val place = placesViewModel.findByPlaceId(placeId)
    val isOwner = place?.createBy == userId
    val isAdmin = userRole == com.example.app.model.Role.ADMIN
    
    val favorites by favoritesViewModel.favorites.collectAsState()
    val isFavorite = remember(placeId, favorites, userId) {
        favoritesViewModel.isFavorite(userId, placeId)
    }
    
    LaunchedEffect(placeId, favorites, userId) {
    }
    var showMoreInfo by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    var selectedRating by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val friends = remember(userId) {
        friendsViewModel.getFriendsByUserId(userId)
    }
    val allUsers by usersViewModel.users.collectAsState()
    val friendUsers = remember(friends, allUsers) {
        friends.mapNotNull { friend ->
            allUsers.find { it.userId == friend.friendId }
        }
    }
    
    val favoriteAddedMessage = stringResource(R.string.favorite_added_success)
    val commentPublishedMessage = stringResource(R.string.comment_published_success)
    val completeRatingMessage = stringResource(R.string.complete_rating_comment)
    val placeDeletedMessage = stringResource(R.string.place_deleted_success)
    val placeSharedMessage = stringResource(R.string.place_shared_success)
    
    val reviews by reviewsViewModel.reviews.collectAsState()
    val placeReviews = remember(placeId, reviews) {
        reviewsViewModel.findByPlaceId(placeId)
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { snackbarData ->
                        Snackbar(
                            snackbarData = snackbarData,
                            containerColor = OrangeDeep,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                )
            }
        ) { paddingValues ->
        if (place == null) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
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
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(Modifier.height(8.dp))
                    }
                    
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
                    
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = OrangeDeep.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
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
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(id = R.drawable.place)
                                )
                                
                                Spacer(Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    if (!isAdmin) {
                                        IconButton(
                                            onClick = {
                                                if (isFavorite) {
                                                    favoritesViewModel.removeFavorite(userId, placeId)
                                                } else {
                                                    favoritesViewModel.addFavorite(userId, placeId)
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = favoriteAddedMessage,
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }
                                                }
                                            }
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.favorite_24),
                                                contentDescription = "Añadir a favoritos",
                                                colorFilter = ColorFilter.tint(
                                                    if (isFavorite) OrangeDeep else Color.Gray
                                                ),
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                        
                                        IconButton(
                                            onClick = {
                                                showCommentDialog = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Comment,
                                                contentDescription = "Comentar",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                        
                                        IconButton(
                                            onClick = {
                                                showShareDialog = true
                                            }
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.send_24),
                                                contentDescription = "Compartir lugar",
                                                colorFilter = ColorFilter.tint(Color.Gray),
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                    
                                    if (isOwner) {
                                        IconButton(
                                            onClick = {
                                                onEdit(placeId)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar lugar",
                                                tint = Orange,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                        
                                        IconButton(
                                            onClick = {
                                                placesViewModel.delete(placeId)
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = placeDeletedMessage,
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                                onDelete()
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar lugar",
                                                tint = Orange,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(Modifier.height(12.dp))
                                
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
                    
                    item {
                        TextButton(
                            onClick = { showMoreInfo = !showMoreInfo },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (showMoreInfo) "Ver menos" else "Ver más",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = OrangeDeep,
                                fontFamily = MontserratFamily
                            )
                        }
                    }
                    
                    if (showMoreInfo) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
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
                                    
                                    HorizontalDivider(
                                        color = Color.Gray.copy(alpha = 0.2f),
                                        thickness = 1.dp
                                    )
                                    
                                    Column {
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
                                    
                                    HorizontalDivider(
                                        color = Color.Gray.copy(alpha = 0.2f),
                                        thickness = 1.dp
                                    )
                    
                                    Column {
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
                                            text = if (schedule.openDay == schedule.closeDay) {
                                                schedule.openDay
                                            } else {
                                                "${schedule.openDay} - ${schedule.closeDay}"
                                            },
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontFamily = MontserratFamily,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${schedule.openTime} - ${schedule.closeTime}",
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
                        }
                    }
                    
                    if (placeReviews.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(16.dp))
                        }
                        
                        items(placeReviews) { review ->
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
                                        text = review.userName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontFamily = MontserratFamily
                                    )
                                    
                                    Spacer(Modifier.height(8.dp))
                                    
                                    Row(
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        repeat(5) { index ->
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = if (index < review.rating.toInt())
                                                    OrangeDeep
                                                else
                                                    Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    
                                    Spacer(Modifier.height(12.dp))
                                    
                                    Text(
                                        text = review.comment,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontFamily = MontserratFamily,
                                        lineHeight = 20.sp
                                    )
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
        
        if (showCommentDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showCommentDialog = false
                    commentText = ""
                    selectedRating = 0
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Escribir comentario",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MontserratFamily
                        )
                        IconButton(
                            onClick = {
                                showCommentDialog = false
                                commentText = ""
                                selectedRating = 0
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Calificación",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = MontserratFamily,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(5) { index ->
                                IconButton(
                                    onClick = { selectedRating = index + 1 },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Estrella ${index + 1}",
                                        tint = if (index < selectedRating)
                                            OrangeDeep
                                        else
                                            Color.Gray,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                        
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = "Escribe tu comentario",
                                    fontFamily = MontserratFamily
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Comparte tu experiencia...",
                                    fontFamily = MontserratFamily
                                )
                            },
                            minLines = 4,
                            maxLines = 6,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangeDeep,
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (commentText.isNotBlank() && selectedRating > 0) {
                                val newReview = com.example.app.model.Review(
                                    id = java.util.UUID.randomUUID().toString(),
                                    userId = "1", // TODO: Obtener del usuario actual
                                    userName = "Usuario", // TODO: Obtener del usuario actual
                                    rating = selectedRating.toDouble(),
                                    comment = commentText,
                                    placeId = placeId
                                )
                                reviewsViewModel.create(newReview)
                                
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = commentPublishedMessage,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                
                                showCommentDialog = false
                                commentText = ""
                                selectedRating = 0
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = completeRatingMessage,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeDeep
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Publicar",
                            fontFamily = MontserratFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showCommentDialog = false
                            commentText = ""
                            selectedRating = 0
                        }
                    ) {
                        Text(
                            text = "Cancelar",
                            fontFamily = MontserratFamily,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
        
        if (showShareDialog && place != null) {
           SharePlaceWithFriendDialog(
               place = place,
               friendUsers = friendUsers,
               userId = userId,
               messagesViewModel = messagesViewModel,
               onDismiss = { showShareDialog = false },
               onShareSuccess = {
                   scope.launch {
                       snackbarHostState.showSnackbar(
                           message = placeSharedMessage,
                           duration = SnackbarDuration.Short
                       )
                   }
                   showShareDialog = false
               }
           )
       }
   }
}

@Composable
private fun SharePlaceWithFriendDialog(
    place: com.example.app.model.Place,
    friendUsers: List<com.example.app.model.User>,
    userId: String,
    messagesViewModel: MessagesViewModel,
    onDismiss: () -> Unit,
    onShareSuccess: () -> Unit
) {
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
            if (friendUsers.isEmpty()) {
                Text(
                    text = "No tienes amigos para compartir",
                    fontFamily = MontserratFamily
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(friendUsers) { friend ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    messagesViewModel.sharePlace(userId, friend.userId, place.id, place.placeName)
                                    onShareSuccess()
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
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    color = Orange.copy(alpha = 0.15f)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Orange,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                
                                Spacer(Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = friend.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontFamily = MontserratFamily
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "@${friend.username}",
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

@Preview(showBackground = true)
@Composable
private fun PlaceDetailPreview() {
    AppTheme {
        PlaceDetail(
            placeId = "1",
            placesViewModel = PlacesViewModel(),
            messagesViewModel = MessagesViewModel()
        )
    }
}

