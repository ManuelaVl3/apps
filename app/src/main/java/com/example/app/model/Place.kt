package com.example.app.model

data class Place(
    val id: String,
    val images: List<String>,
    val placeName: String,
    val description: String,
    val phones: List<String>,
    val type: PlaceType,
    val schedules: List<Schedule>,
    val location: Location,
    val address: String,
    val createBy: String,
    val rating: Double = (1..5).random().toDouble(),
    val status: PlaceStatus = PlaceStatus.PENDING
) {
}