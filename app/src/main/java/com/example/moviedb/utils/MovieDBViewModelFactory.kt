package com.example.moviedb.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.repository.MovieRepository
import com.example.moviedb.viewmodel.MovieDBViewModel

class MovieDBViewModelFactory(
    private val repository: MovieRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDBViewModel(repository, connectivityObserver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

