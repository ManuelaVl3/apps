package com.example.app.model

data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val placeId: String? = null, // ID del lugar compartido, si es null es un mensaje de texto
    val timestamp: Long = System.currentTimeMillis()
)

