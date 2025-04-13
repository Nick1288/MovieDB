package com.example.moviedb.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MovieReview(
    val author: String,
    val content: String,
    val rating: Float
)

class MovieReviewViewModel : ViewModel() {

    private val _reviews = MutableStateFlow<List<MovieReview>>(emptyList())
    val reviews: StateFlow<List<MovieReview>> = _reviews

    init {
        loadDummyReviews()
    }

    private fun loadDummyReviews() {
        _reviews.value = listOf(
            MovieReview("Alice", "Amazing storyline and visuals. A must-watch!", 4.5f),
            MovieReview("Bob", "Great character development, especially the lead role.", 4.0f),
            MovieReview("Charlie", "A bit slow at parts but still enjoyable overall.", 3.5f)
        )
    }
}
