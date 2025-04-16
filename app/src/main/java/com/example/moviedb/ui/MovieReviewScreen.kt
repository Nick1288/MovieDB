package com.example.moviedb.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviedb.model.Review

@Composable
fun MovieReviewScreen(
    reviews: List<Review>,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(reviews) { review ->
            ReviewCard(review = review)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = review.author, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rating: ${review.authorDetails.rating}/10", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = review.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

