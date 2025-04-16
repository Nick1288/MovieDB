package com.example.moviedb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.database.Movies
import com.example.moviedb.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.moviedb.model.MovieCategory

enum class MovieCategory {
    POPULAR,
    TOP_RATED,
    FAVORITES
}

data class MovieListUiState(
    val movies: List<Movie> = emptyList(),
    val currentCategory: MovieCategory = MovieCategory.POPULAR
)

class MovieListViewModel : ViewModel() {

    private val _movieList = MutableStateFlow(MovieListUiState())
    val movieList: StateFlow<MovieListUiState> = _movieList

    init {
        loadMovies(MovieCategory.POPULAR)
    }

    fun loadMovies(category: MovieCategory) {
        viewModelScope.launch {
            val movies = Movies().getMovies(category)
            _movieList.update {
                it.copy(
                    movies = movies,
                    currentCategory = category
                )
            }
        }
    }
}

