package com.example.moviedb.model

data class ReviewListResponse(
    val id: String,
    val results: List<Review>
)
