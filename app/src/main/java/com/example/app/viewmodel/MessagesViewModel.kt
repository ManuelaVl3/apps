package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessagesViewModel : ViewModel() {

    private val _messages = MutableStateFlow(emptyList<Message>())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    init {
        loadMessages()
    }

    fun loadMessages() {
        // Datos de ejemplo: mensajes entre usuario "1" y "2"
        _messages.value = listOf(
            Message(
                id = "1",
                senderId = "1",
                receiverId = "2",
                content = "¡Hola! ¿Has visitado algún lugar interesante últimamente?",
                timestamp = System.currentTimeMillis() - 86400000 // Hace 1 día
            ),
            Message(
                id = "2",
                senderId = "2",
                receiverId = "1",
                content = "Sí, fui al Museo del Oro Quimbaya, está increíble",
                timestamp = System.currentTimeMillis() - 82800000 // Hace 23 horas
            ),
            Message(
                id = "3",
                senderId = "1",
                receiverId = "2",
                content = "¡Genial! Me encantaría visitarlo",
                placeId = "2", // Compartiendo el lugar del museo
                timestamp = System.currentTimeMillis() - 3600000 // Hace 1 hora
            )
        )
    }

    fun getMessagesBetweenUsers(userId1: String, userId2: String): List<Message> {
        return _messages.value.filter { message ->
            (message.senderId == userId1 && message.receiverId == userId2) ||
            (message.senderId == userId2 && message.receiverId == userId1)
        }.sortedBy { it.timestamp }
    }

    fun sendMessage(senderId: String, receiverId: String, content: String, placeId: String? = null) {
        val newMessage = Message(
            id = java.util.UUID.randomUUID().toString(),
            senderId = senderId,
            receiverId = receiverId,
            content = content,
            placeId = placeId,
            timestamp = System.currentTimeMillis()
        )
        _messages.value = _messages.value + newMessage
    }

    fun sharePlace(senderId: String, receiverId: String, placeId: String, placeName: String) {
        val content = "Te comparto este lugar: $placeName"
        sendMessage(senderId, receiverId, content, placeId)
    }
}

