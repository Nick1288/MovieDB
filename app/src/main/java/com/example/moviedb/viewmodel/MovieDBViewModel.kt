package com.example.moviedb.viewmodel
import androidx.lifecycle.ViewModel
import com.example.moviedb.database.MovieDBUiState
import com.example.moviedb.database.Movies
import com.example.moviedb.model.Movie
import com.example.moviedb.model.MovieCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieDBViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MovieDBUiState())
    val uiState: StateFlow<MovieDBUiState> = _uiState.asStateFlow()

    init {
        setSelectedCategory(MovieCategory.POPULAR) // 👈 Initial load!
    }

    fun setSelectedMovie(movie: Movie){
        _uiState.update { currentState ->
            currentState.copy(selectedMovie = movie)
        }
    }

    fun setSelectedCategory(category: MovieCategory){
        val movies = Movies().getMovies(category)
        _uiState.update { current ->
            current.copy(
                selectedCategory = category,
                movies = movies
            )
        }
    }


}