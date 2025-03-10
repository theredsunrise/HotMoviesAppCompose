package com.example.hotmovies.presentation.movies.list.views

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import com.example.hotmovies.presentation.movies.dtos.MovieUIState
import com.example.hotmovies.presentation.shared.helpers.PreviewSharingTransitionScreen
import com.example.hotmovies.presentation.shared.rememberAnimatedImageState
import com.example.hotmovies.presentation.shared.safeClickable
import com.example.hotmovies.presentation.shared.views.AnimatedImage
import com.example.hotmovies.presentation.shared.views.RatingBar
import com.example.hotmovies.shared.Event
import kotlin.math.roundToInt


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MovieListItem(
    modifier: Modifier,
    imageHeight: Dp,
    movie: MovieUIState,
    onLoadSuccess: (movie: MovieUIState) -> Unit,
    onMovieClick: (movie: MovieUIState) -> Unit
) {
    val sizeResolver = rememberConstraintsSizeResolver()
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current).data(
            movie.finalBackDropImageUrl
        ).size(sizeResolver).build(),
        contentScale = ContentScale.Crop,
    )
    val animatedImageState = painter.rememberAnimatedImageState(!movie.isLoaded.hasBeenHandled) {
        onLoadSuccess(movie)
    }

    val shape = MaterialTheme.shapes.medium
    Column(
        modifier
            .graphicsLayer(clip = true, shape = shape)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, shape)
            .safeClickable(isEnabled = animatedImageState.value.isSuccess) {
                onMovieClick(movie)
            }
            .background(MaterialTheme.colorScheme.secondaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        AnimatedImage(
            Modifier
                .fillMaxWidth()
                .requiredHeight(imageHeight)
                .then(sizeResolver),
            animatedImageState,
            true
        )
        RatingBar(
            Modifier
                .requiredSize(110.dp, 17.dp)
                .align(Alignment.Start)
                .padding(start = 8.dp)
                .offset {
                    IntOffset(
                        0,
                        -8.dp
                            .toPx()
                            .roundToInt()
                    )
                },
            5,
            movie.voteAverage,
            true
        )
        Text(
            movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp, start = 8.dp, end = 8.dp)
                .skipToLookaheadSize(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center
        )
        Text(
            movie.overview,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, start = 10.dp, end = 8.dp, bottom = 20.dp)
                .skipToLookaheadSize(),
            maxLines = 3,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = "spec:parent=pixel_5")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewMovieListItem() {
    PreviewSharingTransitionScreen {
        val movie = MovieUIState(
            56,
            1,
            "backdrop",
            "Test title 1234567890",
            "Original title",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
            4f,
            Event(false)
        )

        MovieListItem(
            Modifier
                .requiredWidth(200.dp)
                .wrapContentHeight(),
            150.dp,
            movie,
            onLoadSuccess = {}
        ) {}
    }
}



