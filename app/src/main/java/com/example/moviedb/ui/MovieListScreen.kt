package com.example.moviedb.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.moviedb.model.Movie
import com.example.moviedb.utils.Constants
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.moviedb.model.MovieCategory
import androidx.compose.material3.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    movieList:List<Movie>,
    onMovieListItemClicked:(Movie)->Unit,
    modifier: Modifier = Modifier
) {

        MovieList(
            movieList = movieList,
            onMovieListItemClicked=onMovieListItemClicked,
            modifier = modifier
        )
    }

@Composable
fun MovieTabs(
    selectedTab: MovieCategory,
    onTabSelected: (MovieCategory) -> Unit
) {
    val tabs = MovieCategory.entries.filter { it != MovieCategory.FAVORITES } + MovieCategory.FAVORITES

    TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
        tabs.forEachIndexed { index, category ->
            Tab(
                selected = selectedTab == category,
                onClick = { onTabSelected(category) },
                text = { Text(category.displayName()) }
            )
        }
    }
}

fun MovieCategory.displayName(): String = when (this) {
    MovieCategory.POPULAR -> "Popular Movies"
    MovieCategory.TOP_RATED -> "Top Movies"
    MovieCategory.FAVORITES -> "Favourite Movies"
}

@Composable
fun MovieList(movieList: List<Movie>, onMovieListItemClicked: (Movie) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(movieList) { movie ->
            MovieListItemCard(movie = movie, onMovieListItemClicked=onMovieListItemClicked, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun MovieListItemCard(movie: Movie, onMovieListItemClicked: (Movie) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier,
        onClick= {onMovieListItemClicked(movie)}) {
        Row {
            Box {
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_BASE_WIDTH + movie.poster_path,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .width(92.dp)
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = movie.release_date,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}
