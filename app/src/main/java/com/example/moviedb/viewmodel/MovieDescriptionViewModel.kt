package com.example.moviedb.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moviedb.database.MovieDescriptionDB
import com.example.moviedb.model.MovieDescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieDescriptionViewModel : ViewModel() {

    private val _movieDescription = MutableStateFlow<MovieDescription?>(null)
    val movieDescription: StateFlow<MovieDescription?> = _movieDescription

    fun loadMovieDescription(id: Long) {
        _movieDescription.value = MovieDescriptionDB().getMovieDescriptionById(id)
    }
}