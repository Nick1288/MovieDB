package com.example.moviedb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb.model.MovieCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM cached_movies WHERE category = :category")
    fun getMoviesByCategory(category: MovieCategory): Flow<List<CachedMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<CachedMovieEntity>)

    @Query("DELETE FROM cached_movies WHERE category != :category")
    suspend fun deleteMoviesNotInCategory(category: String)
}
