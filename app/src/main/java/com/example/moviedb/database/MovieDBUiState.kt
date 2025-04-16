package com.example.moviedb.database

import com.example.moviedb.model.Movie
import com.example.moviedb.model.MovieCategory

data class MovieDBUiState
    (
    val selectedMovie: Movie?= null,
    val selectedCategory: MovieCategory = MovieCategory.POPULAR,
    val movies: List<Movie> = emptyList(),
    )