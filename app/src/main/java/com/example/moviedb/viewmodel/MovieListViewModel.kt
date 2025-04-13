package com.example.moviedb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.database.Movies
import com.example.moviedb.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    private val _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList: StateFlow<List<Movie>> = _movieList

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            // Replace this with real API or repository logic
            _movieList.value = Movies().getMovies()
        }
    }
}
