package com.example.moviedb.model

data class Review(
    val author: String = "",
    val author_details: AuthorDetails,
    val content: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val url: String = ""
)

data class AuthorDetails(
    val name: String,
    val username: String,
    val avatar_path: String?,
    val rating: Double? // Nullable since some users may not have rated
)
