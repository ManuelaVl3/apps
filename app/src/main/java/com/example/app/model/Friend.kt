package com.example.app.model

data class Friend(
    val id: String,
    val userId: String, // ID del usuario que tiene el amigo
    val friendId: String, // ID del amigo
    val status: FriendStatus = FriendStatus.ACCEPTED // Estado de la amistad
)

enum class FriendStatus {
    PENDING,    // Solicitud pendiente
    ACCEPTED,   // Amistad aceptada
    BLOCKED     // Usuario bloqueado
}

