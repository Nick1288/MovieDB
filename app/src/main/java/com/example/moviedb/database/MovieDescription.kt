package com.example.moviedb.database

import com.example.moviedb.model.MovieDescription
import com.example.moviedb.model.Genre

class MovieDescriptionDB {
        fun getMovieDescriptionById(id: Long): MovieDescription {
            return when (id) {
                1L -> MovieDescription(
                    id = 1,
                    title = "A Minecraft Movie",
                    overview = "A story about the atomic bomb...",
                    genres = listOf(Genre(18, "Drama"), Genre(36, "History")),
                    homepage = "https://www.universalpictures.com/movies/oppenheimer",
                    imdbId = "tt15398776",
                    releaseDate = "2023-07-21",
                    posterPath = "/yFHHfHcUgGAxziP1C3lLt0q2T4s.jpg",
                )
                // Add 4 more hardcoded MovieDescriptions here for your current 5 movies
                else -> throw IllegalArgumentException("No movie found")
            }
        }
}
