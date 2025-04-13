package com.example.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.moviedb.ui.MovieDescriptionScreen
import com.example.moviedb.ui.MovieListScreen
import com.example.moviedb.ui.MovieReviewScreen
import com.example.moviedb.ui.theme.MovieDBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieDBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    //innerPadding -> MovieListScreen(modifier = Modifier.padding(innerPadding))
                    //innerPadding -> MovieDescriptionScreen(movieId = 1,modifier=Modifier.padding(innerPadding))
                    innerPadding -> MovieReviewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}