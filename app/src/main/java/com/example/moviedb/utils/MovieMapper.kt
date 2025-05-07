package com.example.moviedb.utils

import com.example.moviedb.database.CachedMovieEntity
import com.example.moviedb.model.Movie
import com.example.moviedb.model.MovieCategory

// Movie → CachedMovieEntity
fun Movie.toCachedEntity(category: MovieCategory): CachedMovieEntity {
    return CachedMovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = poster_path,
        releaseDate = release_date,
        category = category
    )
}

// CachedMovieEntity → Movie
fun CachedMovieEntity.toDomainModel(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        poster_path = posterPath,
        release_date = releaseDate,
        backdrop_path = "",
        genres = emptyList(),
        homepage = "",
        imdb_id = ""
    )
}