package com.example.app.model

data class Review(
    val id: String,
    val userId: String,
    val userName: String,
    val rating: Double,
    val comment: String,
    val placeId: String
) {
}
