package com.example.app.model

import java.time.LocalDateTime

data class Review (
    val id: String,
    val userId: String,
    val placeId: String,
    val body: String,
    val date: LocalDateTime,
    val rating: Int
){
}