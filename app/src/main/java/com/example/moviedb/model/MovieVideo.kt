package com.example.moviedb.model

data class VideoResponse(
    val id: Int,
    val results: List<MovieVideo>
)

data class MovieVideo(
    val id: String,
    val key: String,   // this is the YouTube key
    val name: String,
    val site: String,  // "YouTube"
    val type: String   // "Trailer", "Teaser", etc
)