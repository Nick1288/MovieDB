package com.example.moviedb.model

data class MovieDescription(
    val id: Long,
    val title: String,
    val overview: String,
    val genres: List<Genre>,
    val homepage: String?,         // nullable in the API
    val imdbId: String?,          // nullable in the API
    val releaseDate: String,
    val posterPath: String
)
