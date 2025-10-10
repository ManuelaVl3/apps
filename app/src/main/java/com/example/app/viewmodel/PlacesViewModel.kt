package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.model.Location
import com.example.app.model.Place
import com.example.app.model.PlaceType
import com.example.app.model.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlacesViewModel: ViewModel() {

    private val _places = MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    init{
        loadPlaces()
    }

    fun loadPlaces(){
        _places.value = listOf(
            Place(
                id = "1",
                images = listOf("https://example.com/image1.jpg"),
                placeName = "Restaurante El Buen Sabor",
                description = "Restaurante familiar con comida típica colombiana y ambiente acogedor",
                phones = listOf("+57 300 123 4567"),
                type = PlaceType.RESTAURANT,
                schedules = listOf(
                    Schedule("Lunes", "11:00", "22:00"),
                    Schedule("Martes", "11:00", "22:00"),
                    Schedule("Miércoles", "11:00", "22:00"),
                    Schedule("Jueves", "11:00", "22:00"),
                    Schedule("Viernes", "11:00", "23:00"),
                    Schedule("Sábado", "11:00", "23:00"),
                    Schedule("Domingo", "12:00", "21:00")
                ),
                location = Location("loc1", 4.5339, -75.6811),
                address = "Calle 19 #12-34, Armenia, Quindío"
            ),

            Place(
                id = "2",
                images = listOf("https://example.com/image2.jpg"),
                placeName = "Museo del Oro Quimbaya",
                description = "Museo que exhibe la cultura prehispánica quimbaya con piezas de oro y cerámica",
                phones = listOf("+57 6 741 8080"),
                type = PlaceType.MUSEUM,
                schedules = listOf(
                    Schedule("Lunes", "09:00", "17:00"),
                    Schedule("Martes", "09:00", "17:00"),
                    Schedule("Miércoles", "09:00", "17:00"),
                    Schedule("Jueves", "09:00", "17:00"),
                    Schedule("Viernes", "09:00", "17:00"),
                    Schedule("Sábado", "09:00", "17:00"),
                    Schedule("Domingo", "10:00", "16:00")
                ),
                location = Location("loc2", 4.5315, -75.6804),
                address = "Carrera 14 #4-70, Armenia, Quindío"
            ),

            Place(
                id = "3",
                images = listOf("https://example.com/image3.jpg"),
                placeName = "Hotel Estelar Armenia, Quindío",
                description = "Hotel de lujo e Armenia con todas las comodidades modernas",
                phones = listOf("+57 6 745 5555", "+57 300 987 6543"),
                type = PlaceType.HOTEL,
                schedules = listOf(
                    Schedule("Lunes", "00:00", "23:59"),
                    Schedule("Martes", "00:00", "23:59"),
                    Schedule("Miércoles", "00:00", "23:59"),
                    Schedule("Jueves", "00:00", "23:59"),
                    Schedule("Viernes", "00:00", "23:59"),
                    Schedule("Sábado", "00:00", "23:59"),
                    Schedule("Domingo", "00:00", "23:59")
                ),
                location = Location("loc3", 4.5368, -75.6825),
                address = "Carrera 14 #21-15, Armenia, Quindío"
            )
        )
    }

    fun create(place: Place){
        _places.value = _places.value + place
    }

    fun findByPlaceId(placeId: String): Place?{
        return _places.value.find { it.id == placeId }
    }

    fun findByType(type: PlaceType): List<Place>{
        return _places.value.filter { it.type == type }
    }

    fun findByName(name: String): List<Place>{
        return _places.value.filter { 
            it.placeName.contains(name, ignoreCase = true) 
        }
    }

    fun findByLocation(location: Location): List<Place>{
        return _places.value.filter { place ->
            val distance = calculateDistance(
                place.location.latitude, place.location.longitude,
                location.latitude, location.longitude
            )
            distance <= 0.1
        }
    }

    fun update(placeId: String, updatedPlace: Place): Boolean {
        val currentPlaces = _places.value.toMutableList()
        val placeIndex = currentPlaces.indexOfFirst { it.id == placeId }
        
        if (placeIndex != -1) {
            currentPlaces[placeIndex] = updatedPlace
            _places.value = currentPlaces
            return true
        }
        return false
    }

    fun delete(placeId: String): Boolean {
        val currentPlaces = _places.value.toMutableList()
        val placeIndex = currentPlaces.indexOfFirst { it.id == placeId }
        
        if (placeIndex != -1) {
            currentPlaces.removeAt(placeIndex)
            _places.value = currentPlaces
            return true
        }
        return false
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        
        return earthRadius * c
    }

}