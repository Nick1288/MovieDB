@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.moviedb.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviedb.R
import com.example.moviedb.viewmodel.MovieDBViewModel


enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Description(title = R.string.movie_description),
    Review(R.string.movie_reviews)
}

@Composable
fun MovieDBAppBar(
    currScreen: MovieDBScreen,
    canNavigateBack:Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = {Text(stringResource(currScreen.title))},
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
fun MovieDbApp(viewModel: MovieDBViewModel = viewModel(),
               navController: NavHostController = rememberNavController()
) {
    val backStackEntity by navController.currentBackStackEntryAsState()
    val uiState by viewModel.uiState.collectAsState()

    val currentScreen = MovieDBScreen.valueOf(
        backStackEntity?.destination?.route ?: MovieDBScreen.List.name
    )

    Scaffold(
        topBar = {
            Column {
                MovieDBAppBar(
                    currScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
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
            composable(route = MovieDBScreen.List.name){
                MovieListScreen(
                    movieList = uiState.movies,
                    onMovieListItemClicked = { movie ->
                        viewModel.setSelectedMovie(movie)
                        navController.navigate(MovieDBScreen.Description.name)
                    },
                    modifier = Modifier.fillMaxSize().padding(16.dp))
            }
            composable(route = MovieDBScreen.Description.name){
                uiState.selectedMovie?.let { movie ->
                    MovieDescriptionScreen(
                        movie = movie,
                        onReviewSelected = {
                            navController.navigate(MovieDBScreen.Review.name)
                        },
                        modifier = Modifier)
                }
            }
            composable(route = MovieDBScreen.Review.name){
                uiState.selectedMovie?.let { movie ->
                    MovieReviewScreen(
                        reviews = viewModel.getReviews(movie.id),
                        modifier = Modifier
                    )
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