@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.moviedb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviedb.R
import com.example.moviedb.viewmodel.MovieDBViewModel
import com.example.moviedb.viewmodel.MovieDescriptionUIState
import com.example.moviedb.viewmodel.MovieListUIState
import com.example.moviedb.viewmodel.MovieReviewUIState
import com.example.moviedb.viewmodel.MovieVideoUIState
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Description(title = R.string.movie_description),
    Review(R.string.movie_reviews)
}

@Composable
fun MovieDBAppBar(
    currScreen: MovieDBScreen,
    canNavigateBack:Boolean,
    lastSynced: Long?,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    val formattedTime = lastSynced?.let {
        val instant = Instant.ofEpochMilli(it)
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } ?: "Not yet synced"
    TopAppBar(
        title = {
            Column {
                Text(stringResource(currScreen.title))
                if (lastSynced != null) {
                    Text(
                        text = "Last updated: $formattedTime",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}
@Composable
fun MovieDbApp(viewModel: MovieDBViewModel,
               navController: NavHostController = rememberNavController()
) {
    val backStackEntity by navController.currentBackStackEntryAsState()
    val uiState by viewModel.uiState.collectAsState()

    //Ui state for each screen
    val movieListUiState by viewModel.movieListUiState.collectAsState()
    val movieDescriptionUiState by viewModel.movieDescriptionUiState.collectAsState()
    val movieReviewUiState by viewModel.movieReviewUiState.collectAsState()
    val movieVideoUiState by viewModel.movieVideoUiState.collectAsState()
    val lastSynced by viewModel.lastSynced.collectAsState()

    val currentScreen = MovieDBScreen.valueOf(
        backStackEntity?.destination?.route ?: MovieDBScreen.List.name
    )

    Scaffold(
        topBar = {
            Column {
                MovieDBAppBar(
                    currScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    lastSynced=lastSynced,
                    navigateUp = { navController.navigateUp() }
                )

                // Only show tabs when on the List screen
                if (currentScreen == MovieDBScreen.List) {
                    MovieTabs(
                        selectedTab = uiState.selectedCategory,
                        onTabSelected = { viewModel.setSelectedCategory(it) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MovieDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = MovieDBScreen.List.name) {
                when (movieListUiState) {
                    is MovieListUIState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is MovieListUIState.Success -> {
                        val movies = (movieListUiState as MovieListUIState.Success).movies
                        MovieGridScreen(
                            movieList = movies,
                            onMovieListItemClicked = { movie ->
                                viewModel.setSelectedMovie(movie)
                                navController.navigate(MovieDBScreen.Description.name)
                            },
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }

                    is MovieListUIState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "No internet connection.\nPlease reconnect to load movies.",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                }
            }
            composable(route = MovieDBScreen.Description.name){
                when (movieDescriptionUiState) {
                    is MovieDescriptionUIState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is MovieDescriptionUIState.Success -> {
                        uiState.selectedMovie?.let { movie ->
                            MovieDescriptionScreen(
                                movie = movie,
                                onReviewSelected = {
                                    viewModel.getReviews(movie.id)
                                    viewModel.getVideos(movie.id)
                                    navController.navigate(MovieDBScreen.Review.name)
                                },
                                modifier = Modifier)
                        }}
                    is MovieDescriptionUIState.Error -> {
                        Text("Failed to load movies.")
                    }
                }
            }
            composable(route = MovieDBScreen.Review.name) {
                when {
                    movieReviewUiState is MovieReviewUIState.Loading || movieVideoUiState is MovieVideoUIState.Loading -> {
                        CircularProgressIndicator()
                    }
                    movieReviewUiState is MovieReviewUIState.Success && movieVideoUiState is MovieVideoUIState.Success -> {
                        val reviews = (movieReviewUiState as MovieReviewUIState.Success).reviews
                        val videos = (movieVideoUiState as MovieVideoUIState.Success).videos

                        MovieReviewScreen(
                            reviews = reviews,
                            videos = videos,
                            modifier = Modifier
                        )
                    }
                    movieReviewUiState is MovieReviewUIState.Error || movieVideoUiState is MovieVideoUIState.Error -> {
                        Text("Failed to load data.")
                    }
                }
            }

        }


    }
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieDBTheme {
        MovieListItemCard(
            movie = Movie(
                2,
                "Captain America: Brave New World",
                "/pzIddUEMWhWzfvLI3TwxUG2wGoi.jpg",
                "/gsQJOfeW45KLiQeEIsom94QPQwb.jpg",
                "2025-02-12",
                "When a group of radical activists take over an energy company's annual gala, seizing 300 hostages, an ex-soldier turned window cleaner suspended 50 storeys up on the outside of the building must save those trapped inside, including her younger brother."
            ), {}
        )
    }
}*/