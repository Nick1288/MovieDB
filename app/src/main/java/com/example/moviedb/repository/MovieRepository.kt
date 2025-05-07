package com.example.moviedb.repository

import com.example.moviedb.network.MovieDBAPI
import com.example.moviedb.database.MovieDao
import com.example.moviedb.utils.toCachedEntity
import com.example.moviedb.utils.toDomainModel
import com.example.moviedb.model.Movie
import com.example.moviedb.model.MovieCategory
import com.example.moviedb.model.MovieListResponse
import com.example.moviedb.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class MovieRepository(
    private val api: MovieDBAPI,
    private val dao: MovieDao
) {

    /**
     * Attempts to fetch movies from the network.
     * Falls back to local Room cache if offline or failed.
     */
    fun getMoviesByCategory(category: MovieCategory): Flow<List<Movie>> = flow {
        if (category == MovieCategory.FAVORITES) {
            preloadFavoritesIfNeeded()
            val favorites = dao.getMoviesByCategory(category).map { it.toDomainModel() }
            emit(favorites)
        } else {
            try {
                val apiResponse = when (category) {
                    MovieCategory.TOP_RATED -> api.retrofitService.getTopRatedMovies(Constants.API_KEY).results
                    MovieCategory.POPULAR -> api.retrofitService.getPopularMovies(Constants.API_KEY).results
                    else -> emptyList()
                }

                dao.deleteMoviesNotInCategory(category.name)
                dao.insertMovies(apiResponse.map { it.toCachedEntity(category) })

                emit(apiResponse)

            } catch (e: IOException) {
                val cached = dao.getMoviesByCategory(category).map { it.toDomainModel() }
                emit(cached)
            }
        }
    }


    val preloadedFavorites = listOf(
        Movie(
            id = 822119,
            title = "Captain America: Brave New World",
            poster_path = "/pzIddUEMWhWzfvLI3TwxUG2wGoi.jpg",
            backdrop_path = "/gsQJOfeW45KLiQeEIsom94QPQwb.jpg",
            release_date = "2025-02-12",
            overview = "After meeting with newly elected U.S. President Thaddeus Ross, Sam finds himself in the middle of an international incident. He must discover the reason behind a nefarious global plot before the true mastermind has the entire world seeing red."
        ),
        Movie(
            id = 533535,
            title = "Deadpool & Wolverine",
            poster_path = "/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg",
            backdrop_path = "/yDHYTfA3R0jFYba16jBB1ef8oIt.jpg",
            release_date = "2024-07-26",
            overview = "A listless Wade Wilson toils away in civilian life with his days as the morally flexible mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade must reluctantly suit-up again with an even more reluctant Wolverine."
        ),
        Movie(
            id = 889737,
            title = "Joker: Folie à Deux",
            poster_path = "/2YHHgzQ1XyS1ZVukooN1V8KZctJ.jpg",
            backdrop_path = "/a44EW6ySOQJdRQz0RJnT8Rz77Fr.jpg",
            release_date = "2024-10-04",
            overview = "While struggling with his dual identity, Arthur Fleck not only stumbles upon true love, but also finds the music that's always been inside him."
        ),
        Movie(
            id = 1022789,
            title = "Inside Out 2",
            poster_path = "/xbK40nno7WJQFtwMf0vOi9Jzv7P.jpg",
            backdrop_path = "/vNcEXbL6wE74ozwWw2SGySB6S33.jpg",
            release_date = "2024-06-14",
            overview = "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone."
        ),
        Movie(
            id = 519182,
            title = "Despicable Me 4",
            poster_path = "/d8dH0ROgt1Oq5AMtfnjGXB7CP8g.jpg",
            backdrop_path = "/h9FoVtDsz1xRKvVoXq7gh6zFGr2.jpg",
            release_date = "2024-07-03",
            overview = "Gru and Lucy and their girls—Margo, Edith and Agnes—welcome a new member to the Gru family, Gru Jr., who is intent on tormenting his dad. Gru also faces a new nemesis in Maxime Le Mal and his femme fatale girlfriend Valentina, forcing the family to go on the run."
        )
    )



    suspend fun preloadFavoritesIfNeeded() {
        val existing = dao.getMoviesByCategory(MovieCategory.FAVORITES)
        if (existing.isEmpty()) {
            dao.insertMovies(preloadedFavorites.map { it.toCachedEntity(MovieCategory.FAVORITES) })
        }
    }




}
