package com.example.moviedb.workers

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moviedb.database.Movies
import com.example.moviedb.model.MovieCategory
import com.example.moviedb.network.MovieDBAPI
import com.example.moviedb.repository.MovieRepository
import android.util.Log


class SyncMoviesWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncMoviesWorker", "WorkManager started syncing movies")
        return try {
            val prefs = applicationContext.getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
            val selectedCategoryName = prefs.getString("selected_category", MovieCategory.POPULAR.name)
            val selectedCategory = MovieCategory.valueOf(selectedCategoryName!!)

            val db = Room.databaseBuilder(
                applicationContext,
                Movies::class.java,
                "movies.db"
            ).build()

            val repository = MovieRepository(
                api = MovieDBAPI,
                dao = db.movieDao(),
            )

            repository.syncMoviesByCategory(selectedCategory)

            Log.d("SyncMoviesWorker", "Sync completed for $selectedCategory")

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}