package com.example.moviedb.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.moviedb.model.Movie
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.moviedb.model.Genre
import com.example.moviedb.model.MovieDescription
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.utils.Constants

@Composable
fun MovieDescriptionScreen(
    movie: Movie,
    modifier: Modifier = Modifier,
) {

    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_BASE_WIDTH + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .width(92.dp)
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = movie.overview)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(text = "Genres:", style = MaterialTheme.typography.titleMedium)
            movie.genres.forEach { genre ->
                Text("- $genre")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            movie.homePage?.let { homepage ->
                val context = LocalContext.current
                Text(
                    text = "Open Homepage",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, homepage.toUri())
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            movie.imdbID?.let { imdbId ->
                val context = LocalContext.current
                Text(
                    text = "Open on IMDB",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        val imdbIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = "https://www.imdb.com/title/$imdbId".toUri()
                            setPackage("com.imdb.mobile") // force the intent to use IMDb app
                        }
                        val fallbackIntent = Intent(Intent.ACTION_VIEW,
                            "https://www.imdb.com/title/$imdbId".toUri())
                        if (imdbIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(imdbIntent)
                        } else {
                            context.startActivity(fallbackIntent)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun MovieDescriptionContent(
    movie: MovieDescription,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movie.overview)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Genres:", style = MaterialTheme.typography.titleMedium)
        movie.genres.forEach { genre ->
            Text("- ${genre.name}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        movie.homepage?.let { homepage ->
            Text(text = "Open Homepage", color = Color.Blue)
        }

        movie.imdbId?.let { imdbId ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Open on IMDB", color = Color.Blue)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDescriptionPreview() {
    MovieDBTheme {
        MovieDescriptionContent(
            MovieDescription(
                id = 1,
                title = "A Minecraft Movie",
                overview = "A story about the atomic bomb...",
                genres = listOf(Genre(18, "Drama"), Genre(36, "History")),
                homepage = "https://www.universalpictures.com/movies/oppenheimer",
                imdbId = "tt15398776",
                releaseDate = "2023-07-21",
                posterPath = "/yFHHfHcUgGAxziP1C3lLt0q2T4s.jpg",
            )
        )
    }
}