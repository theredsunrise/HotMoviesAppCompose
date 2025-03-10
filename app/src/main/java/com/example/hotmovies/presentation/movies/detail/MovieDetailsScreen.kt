package com.example.hotmovies.presentation.movies.detail

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.hotmovies.presentation.movies.detail.viewModel.MovieDetailsViewModel
import com.example.hotmovies.presentation.shared.LocalAppState
import com.example.hotmovies.presentation.shared.LocalNavAnimatedVisibilityScope
import com.example.hotmovies.presentation.shared.floatFractionTransitionStates
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.PROGRESS
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationStates
import com.example.hotmovies.presentation.shared.helpers.PreviewSharingTransitionScreen
import com.example.hotmovies.presentation.shared.isEnabled
import com.example.hotmovies.presentation.shared.rememberAnimatedImageState
import com.example.hotmovies.presentation.shared.views.AnimatedImage
import com.example.hotmovies.presentation.shared.views.CustomPainter
import com.example.hotmovies.presentation.shared.views.CustomTopAppBar
import com.example.hotmovies.presentation.shared.views.LottieFire
import com.example.hotmovies.presentation.shared.views.RatingBar
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.checkMainThread
import com.example.hotmovies.shared.progress


@Stable
@Immutable
data class MovieDetailItem(val movieId: Int, val pageId: Int, val finalBackDropImageUrl: String) {
    val backDropTransitionKey: String = "BackDrop_${movieId}_${pageId}"
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationSpecApi::class
)
@Composable
fun SharedTransitionScope.MovieDetailsScreen(
    modifier: Modifier,
    movieDetailItem: MovieDetailItem,
    movieDetailsState: State<MovieDetailsViewModel.UIState>,
    onAction: (MovieDetailsViewModel.Actions) -> Unit,
    onLegacyBackPress: () -> Unit,
    onError: (exception: Exception) -> Unit
) {

    val currentActivity = LocalActivity.current
    val transitionOverlayFractionProgress =
        LocalNavAnimatedVisibilityScope.current.transition.floatFractionTransitionStates()
    FloatFractionAnimationStates(
        transitionOverlayFractionProgress
    ) { oldState, newState ->
        currentActivity?.isEnabled = newState != PROGRESS
    }

    LaunchedEffect(movieDetailsState.value) {
        checkMainThread()

        val loadMovieDetailsEvent = movieDetailsState.value.loadAction
        if (loadMovieDetailsEvent.content is ResultState.Failure) {
            onError(loadMovieDetailsEvent.content.exception)
            return@LaunchedEffect
        }

        val loadMovieDetailsAction =
            loadMovieDetailsEvent.getContentIfNotHandled() ?: return@LaunchedEffect
        when {
            loadMovieDetailsAction.isSuccessFalse -> onAction(
                MovieDetailsViewModel.Actions.LoadMovieDetails(
                    movieDetailItem.movieId
                )
            )
        }
    }

    Box(
        modifier
            .background(MaterialTheme.colorScheme.background)
    ) {

        ConstraintLayout(
            Modifier
                .matchParentSize()
                .verticalScroll(rememberScrollState())
        ) {

            val (backdropImage, toolbar, clippedView, fireStart, fireEnd,
                posterImage, movieTitle, ratingBar,
                movieOverview) = createRefs()

            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalPlatformContext.current).data(
                    movieDetailItem.finalBackDropImageUrl
                ).build(),
                contentScale = ContentScale.Crop,
            )

            Image(
                painter,
                contentDescription = "Backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(backdropImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.matchParent
                        height = Dimension.value(300.dp)
                    }
            )

            CustomTopAppBar(
                Modifier
                    .background(Color.Transparent)
                    .constrainAs(toolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.matchParent
                        height = Dimension.wrapContent
                    }
                    .skipToLookaheadSize(),
                "",
                !LocalAppState.current.isBackGesturesEnabled,
                onBackPress = onLegacyBackPress
            )

            val pathColor = MaterialTheme.colorScheme.onSecondaryContainer
            Canvas(modifier = Modifier
                .constrainAs(clippedView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(backdropImage.bottom)
                    height = Dimension.value(80.dp)
                    width = Dimension.matchParent
                }
                .skipToLookaheadSize()
            ) {

                val path = Path().apply {
                    moveTo(0f, size.height) // Bottom-left corner (right angle)
                    lineTo(size.width, size.height) // Bottom-right corner
                    lineTo(0f, 0f) // Top-left corner
                    close()
                }
                drawPath(path, pathColor)
            }

            LottieFire(
                modifier = Modifier
                    .constrainAs(fireStart) {
                        width = Dimension.value(30.dp)
                        height = Dimension.value(80.dp)
                        bottom.linkTo(posterImage.top, -(25).dp)
                        start.linkTo(posterImage.start, -(12).dp)
                    }
                    .skipToLookaheadSize(), false
            )

            LottieFire(
                modifier = Modifier
                    .constrainAs(fireEnd) {
                        width = Dimension.value(30.dp)
                        height = Dimension.value(80.dp)
                        bottom.linkTo(posterImage.top, -(25).dp)
                        start.linkTo(posterImage.end, -(18).dp)
                    }
                    .skipToLookaheadSize(), false
            )

            val imageState = rememberLoadPosterImageState(movieDetailsState)
            AnimatedImage(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(3.5.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .constrainAs(posterImage) {
                        width = Dimension.value(90.dp)
                        height = Dimension.value(130.dp)
                        top.linkTo(clippedView.top, (-20).dp)
                        start.linkTo(parent.start, margin = 20.dp)
                    }
                    .skipToLookaheadSize(),
                imageState,
                false
            )

            if (movieDetailsState.value.loadAction.content.isSuccess) {
                Text(
                    text = movieDetailsState.value.movieDetails.title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .constrainAs(movieTitle) {
                            top.linkTo(clippedView.top, margin = 80.dp)
                            start.linkTo(posterImage.end, margin = 20.dp)
                            end.linkTo(parent.end, margin = 20.dp)
                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent
                        }
                        .skipToLookaheadSize()
                )

                RatingBar(
                    modifier = Modifier
                        .constrainAs(ratingBar) {
                            top.linkTo(movieTitle.bottom)
                            start.linkTo(movieTitle.start)
                            width = Dimension.value(90.dp)
                            height = Dimension.value(15.dp)
                        }
                        .skipToLookaheadSize(),
                    rating = movieDetailsState.value.movieDetails.voteAverage,
                    isInverse = true
                )

                Text(
                    movieDetailsState.value.movieDetails.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(20.dp)
                        .constrainAs(movieOverview) {
                            top.linkTo(ratingBar.bottom, margin = 25.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent
                        }
                        .skipToLookaheadSize()
                )
            }
        }
    }
}

@Composable
private fun rememberLoadPosterImageState(movieDetailsState: State<MovieDetailsViewModel.UIState>): State<ResultState<CustomPainter>> {
    val uiState = movieDetailsState.value
    val loadAction = uiState.loadAction.content
    if (loadAction.isSuccessTrue) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalPlatformContext.current).data(
                uiState.movieDetails.posterUrl.orEmpty()
            ).build(),
            contentScale = ContentScale.Crop,
        )
        return painter.rememberAnimatedImageState(!uiState.movieDetails.isLoaded.hasBeenHandled) {
            uiState.movieDetails.isLoaded.getContentIfNotHandled()
        }
    } else {
        return remember { mutableStateOf(progress) }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = "spec:parent=pixel_5")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewRatingBar() {
    PreviewSharingTransitionScreen {

        val movieDetailItem = remember { MovieDetailItem(0, 0, "backDropUrl") }

        MovieDetailsScreen(
            Modifier.fillMaxSize(),
            movieDetailItem,
            remember { mutableStateOf(MovieDetailsViewModel.UIState.defaultState()) },
            onAction = {},
            onLegacyBackPress = {}) {}
    }
}