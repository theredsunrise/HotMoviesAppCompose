package com.example.hotmovies.presentation.movies.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.hotmovies.CustomApplication
import com.example.hotmovies.R
import com.example.hotmovies.presentation.login.navigation.LoginGraph
import com.example.hotmovies.presentation.movies.list.MoviesScreen
import com.example.hotmovies.presentation.movies.list.MoviesScreenErrorSource.LoadMoviesError
import com.example.hotmovies.presentation.movies.list.MoviesScreenErrorSource.LoadUserDetailsError
import com.example.hotmovies.presentation.movies.list.MoviesScreenErrorSource.LogoutError
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.LoadMovies
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.LoadUserDetails
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.Logout
import com.example.hotmovies.presentation.movies.navigation.MoviesGraphRoutes.MovieDetail
import com.example.hotmovies.presentation.movies.navigation.MoviesGraphRoutes.Movies
import com.example.hotmovies.presentation.shared.showDialog
import com.example.hotmovies.presentation.shared.views.CustomDialogState
import kotlinx.serialization.Serializable

@Serializable
data object MoviesGraph

@Serializable
private sealed interface MoviesGraphRoutes {
    @Serializable
    data object Movies : MoviesGraphRoutes

    @Serializable
    data object MovieDetail : MoviesGraphRoutes
}

@SuppressLint("ComposableDestinationInComposeScope")
@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.moviesGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope
) {
    navigation<MoviesGraph>(startDestination = Movies) {
        composable<Movies>(
        ) {
            val viewModel: MoviesViewModel = viewModel {
                MoviesViewModel(CustomApplication.diContainer)
            }

            val currentActivity = LocalActivity.current
            val exitDialogState = showDialog(android.R.string.ok) {
                viewModel.doAction(Logout)
            }

            val errorDialogState = showDialog { source ->
                when (source) {
                    is LogoutError -> viewModel.doAction(Logout)
                    is LoadMoviesError -> viewModel.doAction(LoadMovies)
                    is LoadUserDetailsError -> viewModel.doAction(LoadUserDetails)
                }
            }

            MoviesScreen(
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars.union(WindowInsets.displayCutout)),
                sharedTransitionScope,
                this@composable,
                viewModel.state,
                viewModel.moviesPagingData,
                viewModel::doAction,
                onBack = {
                    exitDialogState.value = CustomDialogState(
                        0,
                        currentActivity?.getString(R.string.dialog_msg_are_you_sure_to_logout)
                            .orEmpty()
                    )
                },
                onLogout = {
                    navController.navigate(LoginGraph) {
                        popUpTo(MoviesGraph) {
                            inclusive = true
                        }
                    }
                }) { source, exception ->
                errorDialogState.value = CustomDialogState(source, exception.message.orEmpty())
            }
        }
        composable<MovieDetail>(
        ) {
        }
    }
}