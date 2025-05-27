package com.example.moviedb
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.work.ExistingWorkPolicy
import com.example.moviedb.database.Movies
import com.example.moviedb.network.MovieDBAPI
import com.example.moviedb.repository.MovieRepository
import com.example.moviedb.ui.MovieDbApp
import com.example.moviedb.ui.MovieDescriptionScreen
import com.example.moviedb.ui.MovieListScreen
import com.example.moviedb.ui.MovieReviewScreen
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.utils.MovieDBViewModelFactory
import com.example.moviedb.utils.NetworkConnectivityObserver
import com.example.moviedb.viewmodel.MovieDBViewModel
import com.example.moviedb.workers.SyncMoviesWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = Room.databaseBuilder(
            applicationContext,
            Movies::class.java,
            "movies.db"
        ).build()

        val repository = MovieRepository(
            api = MovieDBAPI,
            dao = database.movieDao()
        )

        val connectivityObserver = NetworkConnectivityObserver(applicationContext)

        val viewModel = ViewModelProvider(
            this,
            MovieDBViewModelFactory(application, repository, connectivityObserver)
        )[MovieDBViewModel::class.java]

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncMoviesWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "sync_movies_on_network",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )


        setContent {
            MovieDBTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieDbApp(viewModel=viewModel)
                }
            }
            }
        }
    }
