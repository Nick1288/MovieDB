package com.example.moviedb.ui

import androidx.media3.common.MediaItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import android.net.Uri
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moviedb.model.MovieVideo
import com.example.moviedb.model.Review
import androidx.core.net.toUri
import androidx.media3.ui.PlayerView

@Composable
fun MovieReviewScreen(
    reviews: List<Review>?,
    videos: List<MovieVideo>?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Reviews Section
        Text(
            text = "Reviews",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (reviews.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No reviews available.")
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Videos Section
        Text(
            text = "Trailers",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (videos.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No videos available.")
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(videos) { video ->
                    MovieVideoCard(video = video)
                }
            }
        }
    }
}


@Composable
fun ReviewCard(review: Review) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = review.author, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rating: ${review.author_details.rating}/10", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = review.content, style = MaterialTheme.typography.bodyMedium)
        }
    }

}

@Composable
fun MovieVideoCard(video: MovieVideo) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
    ) {
        Column {
            VideoPlayer(
                videoUrl = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3", // Same sample video for all
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Text(
                text = video.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl.toUri())
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                player = exoPlayer
            }
        }, modifier = modifier)
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}
