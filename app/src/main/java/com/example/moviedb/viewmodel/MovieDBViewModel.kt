package com.example.moviedb.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.database.MovieDBUiState
import com.example.moviedb.model.Movie
import com.example.moviedb.model.MovieCategory
import com.example.moviedb.model.Review
import com.example.moviedb.network.MovieDBAPI
import com.example.moviedb.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

class MovieDBViewModel: ViewModel() {

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

    init {
        setSelectedCategory(MovieCategory.POPULAR) //initialisation with popular
    }

    fun setSelectedMovie(movie: Movie){
        _movieDescriptionUiState.value = MovieDescriptionUIState.Loading
        viewModelScope.launch {
            try {
                val response = MovieDBAPI.retrofitService.getMovieDetails(movie.id, Constants.API_KEY)

                _uiState.update { currentState ->
                    currentState.copy(selectedMovie = response)
                }
                _movieDescriptionUiState.value = MovieDescriptionUIState.Success(_uiState.value.selectedMovie)

            } catch (e: Exception) {
                _movieListUiState.value = MovieListUIState.Error
                e.printStackTrace()
            }
        }

    }

    fun setSelectedCategory(category: MovieCategory){
        _movieListUiState.value = MovieListUIState.Loading
        viewModelScope.launch {
            try {
                val response = when (category) {
                    MovieCategory.POPULAR -> MovieDBAPI.retrofitService.getPopularMovies(Constants.API_KEY).results
                    MovieCategory.TOP_RATED -> MovieDBAPI.retrofitService.getTopRatedMovies(Constants.API_KEY).results
                    MovieCategory.FAVORITES -> listOf()
                }

                _uiState.update { current ->
                    current.copy(
                        selectedCategory = category,
                        movies = response
                    )
                }
                _movieListUiState.value = MovieListUIState.Success(_uiState.value.movies)

            } catch (e: Exception) {
                _movieListUiState.value = MovieListUIState.Error
                e.printStackTrace()
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
                _movieListUiState.value = MovieListUIState.Error
                e.printStackTrace()
            }
        }

    }


}