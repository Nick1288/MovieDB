package com.example.moviedb.viewmodel
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import android.content.Context
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
import com.example.moviedb.workers.SyncMoviesWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.core.content.edit
import androidx.work.ExistingWorkPolicy
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart

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

class MovieDBViewModel(
    application: Application,
    private val repository: MovieRepository,
    private val connectivityObserver: ConnectivityObserver,
) : AndroidViewModel(application){
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

    private var currentCategoryJob: Job? = null

    private val _lastSynced = MutableStateFlow<Long?>(null)
    val lastSynced: StateFlow<Long?> = _lastSynced

    // use shared preference to store selected category even when the app is restarted or killed
    init {
        observeConnectivity()

        val prefs = getApplication<Application>().getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)

        // Only default to POPULAR if no previously saved category
        val savedCategory = if (prefs.contains("selected_category")) {
            val saved = prefs.getString("selected_category", MovieCategory.POPULAR.name)
            MovieCategory.valueOf(saved!!)
        } else {
            MovieCategory.POPULAR
        }

        setSelectedCategory(savedCategory)
    }

    private fun observeConnectivity() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncMoviesWorker>()
            .setConstraints(constraints)
            .build()

        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    WorkManager.getInstance(getApplication()).enqueueUniqueWork(
                        "sync_movies_on_network",
                        ExistingWorkPolicy.REPLACE,
                        syncRequest
                    )

                    // ✅ Manually trigger sync again
                    val prefs = getApplication<Application>().getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)

                    _lastSynced.value = System.currentTimeMillis()
                    if (prefs.contains("selected_category")) {
                        delay(300)
                        val saved = prefs.getString("selected_category", MovieCategory.POPULAR.name)
                        val savedCategory = MovieCategory.valueOf(saved!!)
                        setSelectedCategory(savedCategory, skipSync = true)
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

    fun setSelectedCategory(category: MovieCategory, skipSync: Boolean = false) {
        _movieListUiState.value = MovieListUIState.Loading

        viewModelScope.launch {
            val prefs = getApplication<Application>().getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
            prefs.edit { putString("selected_category", category.name) }

            _uiState.update { it.copy(selectedCategory = category) }


            currentCategoryJob?.cancel()

            if (!skipSync) {
                repository.syncMoviesByCategory(category)
                _lastSynced.value = System.currentTimeMillis()
            }

            currentCategoryJob = launch {
                repository.observeCachedMovies(category)
                    .onStart { delay(50) }
                    .collect { result ->
                        _uiState.update {
                            it.copy(selectedCategory = category, cachedMovies = result)
                        }

                        _movieListUiState.value = if (result.isEmpty()) {
                            MovieListUIState.Error
                        } else {
                            MovieListUIState.Success(result)
                        }
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