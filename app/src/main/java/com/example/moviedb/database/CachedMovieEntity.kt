package com.example.moviedb.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviedb.model.MovieCategory

@Entity(tableName = "cached_movies")
data class CachedMovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val category: MovieCategory
)