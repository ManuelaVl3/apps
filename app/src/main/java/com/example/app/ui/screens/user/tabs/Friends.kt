package com.example.app.ui.screens.user.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.MontserratFamily
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.OrangeDeep
import com.example.app.viewmodel.FriendsViewModel
import com.example.app.viewmodel.UsersViewModel

@Composable
fun Friends(
    userId: String = "1",
    friendsViewModel: FriendsViewModel = remember { FriendsViewModel() },
    usersViewModel: UsersViewModel = remember { UsersViewModel() },
    onFriendClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val allUsers by usersViewModel.users.collectAsState()
    val friends by friendsViewModel.friends.collectAsState()
    
    val userFriends = remember(userId, friends) {
        friendsViewModel.getFriendsByUserId(userId)
    }
    
    val friendUsers = remember(userFriends, allUsers) {
        userFriends.mapNotNull { friend ->
            allUsers.find { it.userId == friend.friendId }
        }
    }
    
    val searchableUsers = remember(searchQuery, allUsers, friendUsers, userId) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            allUsers.filter { user ->
                user.userId != userId && // Excluir al usuario actual
                user.role != com.example.app.model.Role.ADMIN && // Excluir moderadores
                !friendUsers.any { it.userId == user.userId } && // Excluir amigos ya agregados
                (user.name.contains(searchQuery, ignoreCase = true) ||
                 user.username.contains(searchQuery, ignoreCase = true) ||
                 user.email.contains(searchQuery, ignoreCase = true))
            }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Amigos",
                    tint = OrangeDeep,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(Modifier.width(12.dp))
                
                Text(
                    text = "Amigos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Buscar usuarios...",
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
            
            Spacer(Modifier.height(24.dp))
            
            if (searchQuery.isNotEmpty()) {
                if (searchableUsers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron usuarios",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            fontFamily = MontserratFamily,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = "Resultados de búsqueda",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(searchableUsers) { user ->
                            UserCard(
                                user = user,
                                isFriend = false,
                                onAddFriend = {
                                    friendsViewModel.addFriend(userId, user.userId)
                                    searchQuery = ""
                                }
                            )
                        }
                    }
                }
            } else {
                if (friendUsers.isEmpty()) {
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
                                text = "No tienes amigos agregados",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontFamily = MontserratFamily,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Busca usuarios para agregarlos como amigos",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontFamily = MontserratFamily,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Mis amigos (${friendUsers.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MontserratFamily
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(friendUsers) { user ->
                            UserCard(
                                user = user,
                                isFriend = true,
                                onClick = {
                                    onFriendClick(user.userId)
                                },
                                onRemoveFriend = {
                                    friendsViewModel.removeFriend(userId, user.userId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    user: com.example.app.model.User,
    isFriend: Boolean,
    onClick: (() -> Unit)? = null,
    onAddFriend: (() -> Unit)? = null,
    onRemoveFriend: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isFriend && onClick != null) Modifier.clickable { onClick() } else Modifier),
        colors = CardDefaults.cardColors(
            containerColor = com.example.app.ui.theme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(28.dp),
                color = Orange.copy(alpha = 0.2f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = OrangeDeep,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = MontserratFamily
                )
                
                Spacer(Modifier.height(4.dp))
                
                Text(
                    text = "@${user.username}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontFamily = MontserratFamily
                )
                
                Spacer(Modifier.height(4.dp))
                
                Text(
                    text = user.city,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontFamily = MontserratFamily
                )
            }
            
            if (isFriend) {
                TextButton(
                    onClick = { onRemoveFriend?.invoke() }
                ) {
                    Text(
                        text = "Eliminar",
                        fontSize = 14.sp,
                        color = Color(0xFFE53935),
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Button(
                    onClick = { onAddFriend?.invoke() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeDeep
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Agregar",
                        fontSize = 14.sp,
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendsPreview() {
    AppTheme {
        Friends()
    }
}
