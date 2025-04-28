package com.example.moviedb.ui

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.moviedb.model.Genre
import com.example.moviedb.model.MovieDescription
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.utils.Constants

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("QueryPermissionsNeeded")
@Composable
fun MovieDescriptionScreen(
    movie: Movie,
    onReviewSelected: ()->Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    AsyncImage(
                        model = Constants.POSTER_IMAGE_BASE_URL + Constants.DESCRIPTION_IMAGE_BASE_WIDTH + movie.poster_path,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
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
            if (movie.genres.isNotEmpty()) {
                Text(
                    text = "Genres:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    overflow = FlowRowOverflow.Visible,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    movie.genres.forEach { genre ->
                        genre?.name?.let { name ->
                            AssistChip(
                                onClick = {},
                                label = { Text(text = name) },
                                colors = AssistChipDefaults.assistChipColors()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            movie.homepage?.let { homepage ->
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
            movie.imdb_id?.let { imdbId ->
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
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth(),
                onClick = onReviewSelected) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reviews",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.weight(1f)) // pushes arrow to the end
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to reviews"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

//for preview
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