package com.example.hotmovies.presentation.movies.list.views

import android.content.res.Configuration
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
import com.example.hotmovies.presentation.movies.dtos.MovieUI
import com.example.hotmovies.presentation.shared.rememberAnimatedImageState
import com.example.hotmovies.presentation.shared.views.AnimatedImage
import com.example.hotmovies.presentation.shared.views.RatingBar
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme
import kotlin.math.roundToInt


@Composable
fun MovieListItem(
    modifier: Modifier,
    imageHeight: Dp,
    movie: MovieUI
) {
    val shape = MaterialTheme.shapes.medium
    Column(
        modifier
            .graphicsLayer(clip = true, shape = shape)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, shape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        val sizeResolver = rememberConstraintsSizeResolver()
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalPlatformContext.current).data(
                movie.backdropUrl ?: movie.posterUrl.orEmpty()
            ).size(sizeResolver).build(),
            contentScale = ContentScale.Crop,
        )
        val animatedImageState = painter.rememberAnimatedImageState()

        AnimatedImage(
            Modifier
                .fillMaxWidth()
                .requiredHeight(imageHeight)
                .then(sizeResolver),
            animatedImageState
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
            movie.voteAverage.let { it.toFloat() * 5f * 0.1f },
            true
        )
        Text(
            movie.title,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp, start = 8.dp, end = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center
        )
        Text(
            movie.overview,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, start = 10.dp, end = 8.dp, bottom = 20.dp),
            maxLines = 3,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = "spec:parent=pixel_5")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewMovieListItem() {
    HotMoviesAppComposeTheme {
        val movie = MovieUI(
            56,
            1,
            "backdrop",
            "Test title 1234567890",
            "Original title",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
            4.0
        )

        MovieListItem(
            Modifier
                .requiredWidth(200.dp)
                .wrapContentHeight(),
            150.dp,
            movie
        )
    }
}



