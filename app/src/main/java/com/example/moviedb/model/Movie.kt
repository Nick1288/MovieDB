package com.example.moviedb.model

data class Movie(
    var id: Long = 0L,
    var title: String="",
    var poster_path: String="",
    var backdrop_path: String="",
    var release_date: String="",
    var overview: String="",
    var genres: List<Genre?> = emptyList(),
    var homepage: String? = "",
    var imdb_id: String? = ""
)
