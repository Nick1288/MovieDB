package com.example.moviedb.model

data class Review(
    val id: String,
    val author: String,
    val authorDetails: AuthorDetails,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val url: String
)

data class AuthorDetails(
    val name: String,
    val username: String,
    val avatarPath: String?,
    val rating: Double? // Nullable since some users may not have rated
)
