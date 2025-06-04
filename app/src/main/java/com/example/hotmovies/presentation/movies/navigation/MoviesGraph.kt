package com.example.hotmovies.presentation.movies.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.hotmovies.CustomApplication
import com.example.hotmovies.R
import com.example.hotmovies.presentation.login.navigation.LoginGraph
import com.example.hotmovies.presentation.movies.detail.MovieDetailItem
import com.example.hotmovies.presentation.movies.detail.MovieDetailsScreen
import com.example.hotmovies.presentation.movies.detail.viewModel.MovieDetailsViewModel
import com.example.hotmovies.presentation.movies.detail.viewModel.MovieDetailsViewModel.Actions.LoadMovieDetails
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
import com.example.hotmovies.presentation.shared.LocalNavAnimatedVisibilityScope
import com.example.hotmovies.presentation.shared.LocalSharedTransitionScope
import com.example.hotmovies.presentation.shared.views.CustomDialogState
import com.example.hotmovies.presentation.shared.views.showDialog
import com.example.hotmovies.shared.Constants
import kotlinx.serialization.Serializable

@Serializable
data object MoviesGraph

@Serializable
private sealed interface MoviesGraphRoutes {
    @Serializable
    data object Movies : MoviesGraphRoutes

    @Serializable
    data class MovieDetail(val movieId: Int, val pageId: Int, val backDropUrl: String) :
        MoviesGraphRoutes
}

@SuppressLint("ComposableDestinationInComposeScope")
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
fun NavGraphBuilder.moviesGraph(
    navController: NavController
) {
    navigation<MoviesGraph>(startDestination = Movies) {
        composable<Movies>(
            enterTransition = {
                fadeIn(
                    initialAlpha = 0.6f,
                    animationSpec = tween(Constants.AnimationDurations.DEFAULT)
                )
            },
            exitTransition = {
                ExitTransition.None
            },
            popEnterTransition = {
                fadeIn(
                    initialAlpha = 0.6f,
                    animationSpec = tween(Constants.AnimationDurations.DEFAULT)
                )
            }
        ) {
            val viewModel: MoviesViewModel = viewModel {
                val diContainer = CustomApplication.diContainer
                MoviesViewModel(
                    diContainer.appContext.resources,
                    diContainer.pager,
                    diContainer.movieImageIdToUrlMapper,
                    diContainer.loginRepository,
                    diContainer.settingsRepository,
                    diContainer.movieDataRepository
                )
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

            with(LocalSharedTransitionScope.current) {
                CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this@composable) {
                    MoviesScreen(
                        Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars.union(WindowInsets.displayCutout)),
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
                        },
                        onMovieClick = {
                            navController.navigate(
                                MovieDetail(
                                    it.id,
                                    it.pageId,
                                    it.finalBackDropImageUrl
                                )
                            )
                        }) { source, exception ->
                        errorDialogState.value =
                            CustomDialogState(source, exception.message.orEmpty())
                    }
                }
            }
        }
        composable<MovieDetail>() {
            val viewModel: MovieDetailsViewModel = viewModel {
                val diContainer = CustomApplication.diContainer
                MovieDetailsViewModel(
                    diContainer.movieDataRepository,
                    diContainer.movieImageIdToUrlMapper
                )
            }

            val movieDetailItem = with(it.toRoute<MovieDetail>()) {
                remember { MovieDetailItem(movieId, pageId, backDropUrl) }
            }

            val errorDialogState = showDialog() {
                viewModel.doAction(LoadMovieDetails(movieDetailItem.movieId))
            }

            with(LocalSharedTransitionScope.current) {
                CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this@composable) {
                    MovieDetailsScreen(
                        Modifier
                            .sharedElement(
                                rememberSharedContentState(key = movieDetailItem.backDropTransitionKey),
                                LocalNavAnimatedVisibilityScope.current,
                                boundsTransform = { initialBounds, finalBounds ->
                                    tween(Constants.AnimationDurations.DEFAULT)
                                },
                                clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium)
                            )
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBars),
                        movieDetailItem,
                        viewModel.state.collectAsStateWithLifecycle(),
                        viewModel::doAction,
                        onLegacyBackPress = { navController.popBackStack() }
                    ) {
                        errorDialogState.value = CustomDialogState(0, it.message.orEmpty())
                    }
                }
            }
        }
    }
}