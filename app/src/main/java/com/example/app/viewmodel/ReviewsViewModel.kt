package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReviewsViewModel: ViewModel() {

    private val _reviews = MutableStateFlow(emptyList<Review>())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    init{
        loadReviews()
    }

    fun loadReviews(){
        _reviews.value = listOf(
            Review(
                id = "1",
                userId = "1",
                placeId = "1",
                body = "Excelente comida colombiana, muy rica y abundante. El servicio es muy atento y el ambiente es acogedor. Definitivamente volveré.",
                date = "2024-01-15 14:30",
                rating = 5
            ),
            
            Review(
                id = "2",
                userId = "2",
                placeId = "1",
                body = "La comida está buena pero un poco cara para lo que es. El lugar está bien decorado y el personal es amable.",
                date = "2024-01-20 19:45",
                rating = 4
            ),
            
            Review(
                id = "3",
                userId = "1",
                placeId = "2",
                body = "Un museo increíble con una colección impresionante de arte prehispánico. Muy educativo y bien organizado. Recomendado para toda la familia.",
                date = "2024-01-18 10:15",
                rating = 5
            ),
            
            Review(
                id = "4",
                userId = "3",
                placeId = "2",
                body = "Interesante exposición sobre la cultura quimbaya. Las piezas están muy bien conservadas y las explicaciones son claras.",
                date = "2024-01-22 16:20",
                rating = 4
            ),
            
            Review(
                id = "5",
                userId = "2",
                placeId = "3",
                body = "Hotel muy cómodo y bien ubicado. Las habitaciones son amplias y limpias. El desayuno está incluido y es muy bueno.",
                date = "2024-01-25 11:00",
                rating = 5
            ),
            
            Review(
                id = "6",
                userId = "1",
                placeId = "3",
                body = "Servicio impecable y habitaciones muy confortables. La piscina está genial y el personal es muy atento. Perfecto para descansar.",
                date = "2024-01-28 13:30",
                rating = 5
            )
        )
    }

    fun create(review: Review){
        _reviews.value = _reviews.value + review
    }

    fun findByReviewId(reviewId: String): Review?{
        return _reviews.value.find { it.id == reviewId }
    }

    fun findByPlaceId(placeId: String): List<Review>{
        return _reviews.value.filter { it.placeId == placeId }
    }

    fun findByUserId(userId: String): List<Review>{
        return _reviews.value.filter { it.userId == userId }
    }

    fun findByRating(rating: Int): List<Review>{
        return _reviews.value.filter { it.rating == rating }
    }

    fun findByName(searchText: String): List<Review>{
        return _reviews.value.filter { 
            it.body.contains(searchText, ignoreCase = true) 
        }
    }

    fun update(reviewId: String, updatedReview: Review): Boolean {
        val currentReviews = _reviews.value.toMutableList()
        val reviewIndex = currentReviews.indexOfFirst { it.id == reviewId }
        
        if (reviewIndex != -1) {
            currentReviews[reviewIndex] = updatedReview
            _reviews.value = currentReviews
            return true
        }
        return false
    }

    fun delete(reviewId: String): Boolean {
        val currentReviews = _reviews.value.toMutableList()
        val reviewIndex = currentReviews.indexOfFirst { it.id == reviewId }
        
        if (reviewIndex != -1) {
            currentReviews.removeAt(reviewIndex)
            _reviews.value = currentReviews
            return true
        }
        return false
    }

    fun getAverageRatingForPlace(placeId: String): Double {
        val placeReviews = findByPlaceId(placeId)
        return if (placeReviews.isNotEmpty()) {
            placeReviews.map { it.rating }.average()
        } else {
            0.0
        }
    }

    fun getReviewsCountForPlace(placeId: String): Int {
        return findByPlaceId(placeId).size
    }

    fun getRatingDistributionForPlace(placeId: String): Map<Int, Int> {
        val placeReviews = findByPlaceId(placeId)
        return placeReviews.groupingBy { it.rating }.eachCount()
    }

}
