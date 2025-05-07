package com.example.moviedb.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.database.MovieDBUiState
import com.example.moviedb.model.Movie
import com.example.moviedb.model.MovieCategory
import com.example.moviedb.model.MovieVideo
import com.example.moviedb.model.Review
import com.example.moviedb.network.MovieDBAPI
import com.example.moviedb.repository.MovieRepository
import com.example.moviedb.utils.ConnectivityObserver
import com.example.moviedb.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface MovieListUIState {
    data class Success(val movies: List<Movie>) : MovieListUIState
    object Error : MovieListUIState
    object Loading : MovieListUIState
}

sealed interface MovieDescriptionUIState {
    data class Success(val movie: Movie?) : MovieDescriptionUIState
    object Error : MovieDescriptionUIState
    object Loading : MovieDescriptionUIState
}

sealed interface MovieReviewUIState {
    data class Success(val reviews: List<Review>?) : MovieReviewUIState
    object Error : MovieReviewUIState
    object Loading : MovieReviewUIState
}

sealed interface MovieVideoUIState {
    data class Success(val videos: List<MovieVideo>?) : MovieVideoUIState
    object Error : MovieVideoUIState
    object Loading : MovieVideoUIState
}

class MovieDBViewModel(private val repository: MovieRepository,
                       private val connectivityObserver: ConnectivityObserver): ViewModel() {

    private val _uiState = MutableStateFlow(MovieDBUiState())
    val uiState: StateFlow<MovieDBUiState> = _uiState.asStateFlow()

    //Movie List API UI States
    private val _movieListUiState = MutableStateFlow<MovieListUIState>(MovieListUIState.Loading)
    val movieListUiState: StateFlow<MovieListUIState> = _movieListUiState

    //Movie Description API UI States
    private val _movieDescriptionUiState = MutableStateFlow<MovieDescriptionUIState>(MovieDescriptionUIState.Loading)
    val movieDescriptionUiState: StateFlow<MovieDescriptionUIState> = _movieDescriptionUiState

    //Movie Reviews API UI States
    private val _movieReviewUiState = MutableStateFlow<MovieReviewUIState>(MovieReviewUIState.Loading)
    val movieReviewUiState: StateFlow<MovieReviewUIState> = _movieReviewUiState

    //Movie Videos API UI States
    private val _movieVideoUiState = MutableStateFlow<MovieVideoUIState>(MovieVideoUIState.Loading)
    val movieVideoUiState: StateFlow<MovieVideoUIState> = _movieVideoUiState

    init {
        observeConnectivity()
        setSelectedCategory(MovieCategory.POPULAR) //initialisation with popular
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    // If category is selected, auto-refresh
                    _uiState.value.selectedCategory.let {
                        setSelectedCategory(it)
                    }
                }
            }
        }
    }

    fun setSelectedMovie(movie: Movie){
        _movieDescriptionUiState.value = MovieDescriptionUIState.Loading
        viewModelScope.launch {
            try {
                val response = MovieDBAPI.retrofitService.getMovieDetails(movie.id, Constants.API_KEY)

                _uiState.update { currentState ->
                    currentState.copy(
                        selectedMovie = response)
                }
                _movieDescriptionUiState.value = MovieDescriptionUIState.Success(_uiState.value.selectedMovie)

            } catch (e: Exception) {
                _movieDescriptionUiState.value = MovieDescriptionUIState.Error
                e.printStackTrace()
            }
        }

    }

    fun setSelectedCategory(category: MovieCategory) {
        _movieListUiState.value = MovieListUIState.Loading

        viewModelScope.launch {
            repository.getMoviesByCategory(category)
                .collect { result ->
                    _uiState.update {
                        it.copy(
                            selectedCategory = category,
                            cachedMovies = result
                        )
                    }

                    if (result.isEmpty()) {
                        _movieListUiState.value = MovieListUIState.Error
                    } else {
                        _movieListUiState.value = MovieListUIState.Success(result)
                    }
                }
        }
    }




    fun getReviews(movieId: Long) {
        _movieReviewUiState.value = MovieReviewUIState.Loading
        viewModelScope.launch {
            try {
                val response = MovieDBAPI.retrofitService.getReviews(movieId, Constants.API_KEY)

                _movieReviewUiState.value = MovieReviewUIState.Success(response.results)

            } catch (e: Exception) {
                _movieReviewUiState.value = MovieReviewUIState.Error
                e.printStackTrace()
            }
        }

    }

    fun getVideos(movieId: Long) {
        _movieVideoUiState.value = MovieVideoUIState.Loading
        viewModelScope.launch {
            try {
                val response = MovieDBAPI.retrofitService.getVideo(movieId, Constants.API_KEY)

                _movieVideoUiState.value = MovieVideoUIState.Success(response.results)

            } catch (e: Exception) {
                _movieVideoUiState.value = MovieVideoUIState.Error
                e.printStackTrace()
            }
        }

    }


}