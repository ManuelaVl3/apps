package com.example.app.model

data class Review (
    val id: String,
    val userId: String,
    val placeId: String,
    val body: String,
    val date: String,
    val rating: Int
){
}