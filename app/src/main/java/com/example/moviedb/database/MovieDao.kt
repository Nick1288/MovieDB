package com.example.moviedb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb.model.MovieCategory

@Dao
interface MovieDao {
    @Query("SELECT * FROM cached_movies WHERE category = :category")
    suspend fun getMoviesByCategory(category: MovieCategory): List<CachedMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<CachedMovieEntity>)

    @Query("DELETE FROM cached_movies WHERE category = :category")
    suspend fun deleteMoviesByCategory(category: MovieCategory)

    @Query("DELETE FROM cached_movies WHERE category != :category")
    suspend fun deleteMoviesNotInCategory(category: String)
}
