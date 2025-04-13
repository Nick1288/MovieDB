package com.example.moviedb.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviedb.viewmodel.MovieReview
import com.example.moviedb.viewmodel.MovieReviewViewModel
import com.example.moviedb.ui.theme.MovieDBTheme

@Composable
fun MovieReviewScreen(
    viewModel: MovieReviewViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val reviews by viewModel.reviews.collectAsState()

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(reviews) { review ->
            ReviewCard(review = review)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ReviewCard(review: MovieReview) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = review.author, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rating: ${review.rating}/5", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = review.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    MovieDBTheme {
        Surface {
            MovieReviewScreen()
        }
    }
}
