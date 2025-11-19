package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.model.Location
import com.example.app.model.Place
import com.example.app.model.PlaceStatus
import com.example.app.model.PlaceType
import com.example.app.model.Schedule
import com.example.app.model.User
import com.example.app.utils.RequestResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PlacesViewModel: ViewModel() {

    private val _places = MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()


    private val _userResult = MutableStateFlow<RequestResult?>(null)
    val userResult: StateFlow<RequestResult?> = _userResult.asStateFlow()

    val db = Firebase.firestore

    init{
        loadPlaces()
    }

    fun loadPlaces(){
        _places.value = listOf(
            Place(
                id = "1",
                images = listOf("https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80"),
                placeName = "Restaurante El Buen Sabor",
                description = "Restaurante familiar con comida típica colombiana y ambiente acogedor",
                phones = listOf("+57 300 123 4567"),
                type = PlaceType.RESTAURANT,
                schedules = listOf(
                    Schedule("Lunes", "11:00", "Lunes", "22:00"),
                    Schedule("Martes", "11:00", "Martes", "22:00"),
                    Schedule("Miércoles", "11:00", "Miércoles", "22:00"),
                    Schedule("Jueves", "11:00", "Jueves", "22:00"),
                    Schedule("Viernes", "11:00", "Viernes", "23:00"),
                    Schedule("Sábado", "11:00", "Sábado", "23:00"),
                    Schedule("Domingo", "12:00", "Domingo", "21:00")
                ),
                location = Location("loc1", 4.5339, -75.6811),
                address = "Calle 19 #12-34, Armenia, Quindío",
                createBy = "1",
                status = PlaceStatus.AUTHORIZED
            ),

            Place(
                id = "2",
                images = listOf("place"),
                placeName = "Museo del Oro Quimbaya",
                description = "Museo que exhibe la cultura prehispánica quimbaya con piezas de oro y cerámica",
                phones = listOf("+57 6 741 8080"),
                type = PlaceType.MUSEUM,
                schedules = listOf(
                    Schedule("Lunes", "09:00", "Lunes", "17:00"),
                    Schedule("Martes", "09:00", "Martes", "17:00"),
                    Schedule("Miércoles", "09:00", "Miércoles", "17:00"),
                    Schedule("Jueves", "09:00", "Jueves", "17:00"),
                    Schedule("Viernes", "09:00", "Viernes", "17:00"),
                    Schedule("Sábado", "09:00", "Sábado", "17:00"),
                    Schedule("Domingo", "10:00", "Domingo", "16:00")
                ),
                location = Location("loc2", 4.5315, -75.6804),
                address = "Carrera 14 #4-70, Armenia, Quindío",
                createBy = "2",
                status = PlaceStatus.AUTHORIZED
            ),

            Place(
                id = "3",
                images = listOf("place"),
                placeName = "Hotel Estelar Armenia, Quindío",
                description = "Hotel de lujo e Armenia con todas las comodidades modernas",
                phones = listOf("+57 6 745 5555", "+57 300 987 6543"),
                type = PlaceType.HOTEL,
                schedules = listOf(
                    Schedule("Lunes", "00:00", "Lunes", "23:00"),
                    Schedule("Martes", "00:00", "Martes", "23:00"),
                    Schedule("Miércoles", "00:00", "Miércoles", "23:00"),
                    Schedule("Jueves", "00:00", "Jueves", "23:00"),
                    Schedule("Viernes", "00:00", "Viernes", "23:00"),
                    Schedule("Sábado", "00:00", "Sábado", "23:00"),
                    Schedule("Domingo", "00:00", "Domingo", "23:00")
                ),
                location = Location("loc3", 4.5368, -75.6825),
                address = "Carrera 14 #21-15, Armenia, Quindío",
                createBy = "1",
                status = PlaceStatus.PENDING
            ),

            Place(
                id = "4",
                images = listOf("place"),
                placeName = "Café Juan Valdez",
                description = "Café tradicional colombiano con ambiente acogedor y wifi gratuito",
                phones = listOf("+57 6 741 1234"),
                type = PlaceType.COFFEESHOP,
                schedules = listOf(
                    Schedule("Lunes", "07:00", "Lunes", "21:00"),
                    Schedule("Martes", "07:00", "Martes", "21:00"),
                    Schedule("Miércoles", "07:00", "Miércoles", "21:00"),
                    Schedule("Jueves", "07:00", "Jueves", "21:00"),
                    Schedule("Viernes", "07:00", "Viernes", "22:00"),
                    Schedule("Sábado", "08:00", "Sábado", "22:00"),
                    Schedule("Domingo", "08:00", "Domingo", "20:00")
                ),
                location = Location("loc4", 4.5320, -75.6820),
                address = "Calle 20 #15-25, Armenia, Quindío",
                createBy = "1",
                status = PlaceStatus.PENDING
            ),

            Place(
                id = "5",
                images = listOf("place"),
                placeName = "Comidas Rápidas El Rincón",
                description = "Comida rápida tradicional con empanadas y arepas deliciosas",
                phones = listOf("+57 300 456 7890"),
                type = PlaceType.FASTFOOD,
                schedules = listOf(
                    Schedule("Lunes", "06:00", "Lunes", "23:00"),
                    Schedule("Martes", "06:00", "Martes", "23:00"),
                    Schedule("Miércoles", "06:00", "Miércoles", "23:00"),
                    Schedule("Jueves", "06:00", "Jueves", "23:00"),
                    Schedule("Viernes", "06:00", "Sábado", "00:00"),
                    Schedule("Sábado", "06:00", "Domingo", "00:00"),
                    Schedule("Domingo", "07:00", "Domingo", "22:00")
                ),
                location = Location("loc5", 4.5350, -75.6830),
                address = "Carrera 19 #22-10, Armenia, Quindío",
                createBy = "2",
            ),

            Place(
                id = "6",
                images = listOf("place"),
                placeName = "Restaurante La Parrilla",
                description = "Asados y parrilladas tradicionales colombianas",
                phones = listOf("+57 6 742 3456"),
                type = PlaceType.RESTAURANT,
                schedules = listOf(
                    Schedule("Lunes", "12:00", "Lunes", "23:00"),
                    Schedule("Martes", "12:00", "Martes", "23:00"),
                    Schedule("Miércoles", "12:00", "Miércoles", "23:00"),
                    Schedule("Jueves", "12:00", "Jueves", "23:00"),
                    Schedule("Viernes", "12:00", "Sábado", "00:00"),
                    Schedule("Sábado", "11:00", "Domingo", "00:00"),
                    Schedule("Domingo", "11:00", "Domingo", "22:00")
                ),
                location = Location("loc6", 4.5340, -75.6815),
                address = "Calle 23 #18-45, Armenia, Quindío",
                createBy = "2",
 //
            ),

            Place(
                id = "7",
                images = listOf("place"),
                placeName = "Panadería El Horno",
                description = "Panadería tradicional con panes frescos y pasteles caseros",
                phones = listOf("+57 6 743 7890"),
                type = PlaceType.FASTFOOD,
                schedules = listOf(
                    Schedule("Lunes", "05:00", "Lunes", "20:00"),
                    Schedule("Martes", "05:00", "Martes", "20:00"),
                    Schedule("Miércoles", "05:00", "Miércoles", "20:00"),
                    Schedule("Jueves", "05:00", "Jueves", "20:00"),
                    Schedule("Viernes", "05:00", "Viernes", "21:00"),
                    Schedule("Sábado", "05:00", "Sábado", "21:00"),
                    Schedule("Domingo", "06:00", "Domingo", "19:00")
                ),
                location = Location("loc7", 4.5310, -75.6840),
                address = "Carrera 16 #25-30, Armenia, Quindío",
                createBy = "1",
            ),

            Place(
                id = "8",
                images = listOf("place"),
                placeName = "Biblioteca Pública de Armenia",
                description = "Biblioteca pública con amplia colección de libros y espacios de lectura",
                phones = listOf("+57 6 741 9999"),
                type = PlaceType.MUSEUM,
                schedules = listOf(
                    Schedule("Lunes", "08:00", "Lunes", "18:00"),
                    Schedule("Martes", "08:00", "Martes", "18:00"),
                    Schedule("Miércoles", "08:00", "Miércoles", "18:00"),
                    Schedule("Jueves", "08:00", "Jueves", "18:00"),
                    Schedule("Viernes", "08:00", "Viernes", "18:00"),
                    Schedule("Sábado", "09:00", "Sábado", "17:00"),
                    Schedule("Domingo", "10:00", "Domingo", "16:00")
                ),
                location = Location("loc8", 4.5375, -75.6790),
                address = "Calle 21 #14-28, Armenia, Quindío",
                createBy = "1",
            ),

            Place(
                id = "9",
                images = listOf("place"),
                placeName = "Café del Parque",
                description = "Café al aire libre en el parque principal con vista al paisaje",
                phones = listOf("+57 300 789 0123"),
                type = PlaceType.COFFEESHOP,
                schedules = listOf(
                    Schedule("Lunes", "06:30", "Lunes", "21:30"),
                    Schedule("Martes", "06:30", "Martes", "21:30"),
                    Schedule("Miércoles", "06:30", "Miércoles", "21:30"),
                    Schedule("Jueves", "06:30", "Jueves", "21:30"),
                    Schedule("Viernes", "06:30", "Viernes", "22:00"),
                    Schedule("Sábado", "07:00", "Sábado", "22:00"),
                    Schedule("Domingo", "07:00", "Domingo", "20:30")
                ),
                location = Location("loc9", 4.5360, -75.6835),
                address = "Parque Sucre, Armenia, Quindío",
                createBy = "1",
                status = PlaceStatus.AUTHORIZED
            ),
            
            // Lugares nuevos para el moderador
            Place(
                id = "10",
                images = listOf("place"),
                placeName = "BUÑUELOS EL PAISA",
                description = "Buñuelos tradicionales colombianos",
                phones = listOf("311 609 7462"),
                type = PlaceType.FASTFOOD,
                schedules = listOf(
                    Schedule("Lunes", "06:00", "Lunes", "20:00"),
                    Schedule("Martes", "06:00", "Martes", "20:00"),
                    Schedule("Miércoles", "06:00", "Miércoles", "20:00"),
                    Schedule("Jueves", "06:00", "Jueves", "20:00"),
                    Schedule("Viernes", "06:00", "Viernes", "21:00"),
                    Schedule("Sábado", "06:00", "Sábado", "21:00"),
                    Schedule("Domingo", "07:00", "Domingo", "19:00")
                ),
                location = Location("loc10", 4.5339, -75.6811),
                address = "Cra 18 #5a - 18",
                createBy = "1",
                rating = 5.0,
                status = PlaceStatus.PENDING
            ),
            
            Place(
                id = "11",
                images = listOf("place"),
                placeName = "BOSCO",
                description = "Restaurante moderno con ambiente acogedor",
                phones = listOf("315 222 2222"),
                type = PlaceType.RESTAURANT,
                schedules = listOf(
                    Schedule("Lunes", "12:00", "Lunes", "22:00"),
                    Schedule("Martes", "12:00", "Martes", "22:00"),
                    Schedule("Miércoles", "12:00", "Miércoles", "22:00"),
                    Schedule("Jueves", "12:00", "Jueves", "22:00"),
                    Schedule("Viernes", "12:00", "Viernes", "23:00"),
                    Schedule("Sábado", "12:00", "Sábado", "23:00"),
                    Schedule("Domingo", "12:00", "Domingo", "21:00")
                ),
                location = Location("loc11", 4.5339, -75.6811),
                address = "Cra 17 # 8 - 47",
                createBy = "2",
                rating = 2.0,
                status = PlaceStatus.AUTHORIZED
            ),
            
            Place(
                id = "12",
                images = listOf("place"),
                placeName = "EL COMPADRE",
                description = "Restaurante con ambiente único",
                phones = listOf("333 555 7788"),
                type = PlaceType.RESTAURANT,
                schedules = listOf(
                    Schedule("Lunes", "11:00", "Lunes", "23:00"),
                    Schedule("Martes", "11:00", "Martes", "23:00"),
                    Schedule("Miércoles", "11:00", "Miércoles", "23:00"),
                    Schedule("Jueves", "11:00", "Jueves", "23:00"),
                    Schedule("Viernes", "11:00", "Viernes", "00:00"),
                    Schedule("Sábado", "11:00", "Sábado", "00:00"),
                    Schedule("Domingo", "11:00", "Domingo", "22:00")
                ),
                location = Location("loc12", 4.5339, -75.6811),
                address = "Av. 19 #2 N - 68",
                createBy = "2",
                rating = 4.0,
                status = PlaceStatus.AUTHORIZED
            ),
            
            Place(
                id = "13",
                images = listOf("place"),
                placeName = "SOL Y LUNA",
                description = "Restaurante con decoración temática",
                phones = listOf("350 555 9874"),
                type = PlaceType.RESTAURANT,
                schedules = listOf(
                    Schedule("Lunes", "12:00", "Lunes", "22:00"),
                    Schedule("Martes", "12:00", "Martes", "22:00"),
                    Schedule("Miércoles", "12:00", "Miércoles", "22:00"),
                    Schedule("Jueves", "12:00", "Jueves", "22:00"),
                    Schedule("Viernes", "12:00", "Viernes", "23:00"),
                    Schedule("Sábado", "12:00", "Sábado", "23:00"),
                    Schedule("Domingo", "12:00", "Domingo", "21:00")
                ),
                location = Location("loc13", 4.5339, -75.6811),
                address = "Av. Centenario #45-12",
                createBy = "1",
                rating = 3.0,
                status = PlaceStatus.AUTHORIZED
            )
        )
    }

    fun create(place: Place) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching {
                createFirebase(place)
            }.fold(
                onSuccess = { RequestResult.Success("Place created successfully") },
                onFailure = { RequestResult.Failure(it.message ?: "Error creating place") }
            )
        }
    }

    private suspend fun createFirebase(place: Place) {
        db.collection("places").add(place).await()
    }

    fun findByPlaceId(placeId: String): Place?{
        return _places.value.find { it.id == placeId }
    }

    fun findByType(type: PlaceType): List<Place>{
        return _places.value.filter { it.type == type }
    }

    fun findByCreator(userId: String): List<Place>{
        return _places.value.filter { it.createBy == userId }
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

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        
        return earthRadius * c
    }

    fun updatePlaceStatus(placeId: String, status: PlaceStatus, rejectionReason: String? = null): Boolean {
        val currentPlaces = _places.value.toMutableList()
        val placeIndex = currentPlaces.indexOfFirst { it.id == placeId }
        
        if (placeIndex != -1) {
            val existingPlace = currentPlaces[placeIndex]
            // Mover el lugar actualizado al final de la lista para que aparezca primero cuando se invierta
            currentPlaces.removeAt(placeIndex)
            val updatedPlace = existingPlace.copy(
                status = status,
                rejectionReason = if (status == PlaceStatus.REJECTED) rejectionReason else null
            )
            currentPlaces.add(updatedPlace)
            _places.value = currentPlaces
            return true
        }
        return false
    }
    
    fun getHistoryPlaces(): List<Place> {
        return _places.value.filter { 
            it.status == PlaceStatus.AUTHORIZED || it.status == PlaceStatus.REJECTED 
        }.reversed() 
    }

    fun getPendingPlaces(): List<Place> {
        return _places.value.filter { it.status == PlaceStatus.PENDING }
    }

    fun getAllPlacesForAdmin(): List<Place> {
        return _places.value
    }

}