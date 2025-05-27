package com.example.moviedb.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.repository.MovieRepository
import com.example.moviedb.viewmodel.MovieDBViewModel

class MovieDBViewModelFactory(
    private val application: Application,
    private val repository: MovieRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDBViewModel(application, repository, connectivityObserver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


