package com.example.hotmovies.presentation.movies.list.views

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.hotmovies.R
import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.presentation.movies.dtos.MovieUIMapper
import com.example.hotmovies.presentation.movies.dtos.MovieUIState
import com.example.hotmovies.presentation.shared.LocalAppState
import com.example.hotmovies.presentation.shared.LocalNavAnimatedVisibilityScope
import com.example.hotmovies.presentation.shared.helpers.PreviewSharingTransitionScreen
import com.example.hotmovies.shared.Constants
import com.example.hotmovies.shared.failure
import com.example.hotmovies.shared.isLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MoviesStaggeredGridView(
    modifier: Modifier,
    numOfColumns: Int,
    pagingDataItemsFlow: Flow<PagingData<MovieUIState>>,
    onMovieClick: (movie: MovieUIState) -> Unit,
    onError: (exception: Exception) -> Unit
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val pagingDataItems = remember(pagingDataItemsFlow) {
        pagingDataItemsFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
    }.collectAsLazyPagingItems()

    val loadErrorState: Throwable? by remember {
        derivedStateOf {
            with(pagingDataItems.loadState.source) {
                refresh.failure ?: prepend.failure ?: append.failure
            }
        }
    }

    LaunchedEffect(loadErrorState) {
        loadErrorState?.also {
            onError(Exception(it))
        }
    }

    val loadSource = pagingDataItems.loadState.source
    val isPrependLoading: Boolean = with(loadSource) { prepend.isLoading || refresh.isLoading }
    val isAppendLoading = with(loadSource) { append.isLoading || refresh.isLoading }

    Column(
        modifier, verticalArrangement = Arrangement.Top
    ) {
        if (isPrependLoading) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
            )
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            columns = StaggeredGridCells.Fixed(numOfColumns),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalItemSpacing = 16.dp
        ) {

            items(
                pagingDataItems.itemCount,
                key = pagingDataItems.itemKey { "${it.id}_${it.pageId}" },
            ) { index ->

                val movie = pagingDataItems[index] ?: return@items
                if (LocalInspectionMode.current) {
                    MovieListItem(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        resolveHeightOfItem(index),
                        movie,
                        onLoadSuccess = { movie.isLoaded.getContentIfNotHandled() },
                        onMovieClick = onMovieClick
                    )
                } else {
                    MovieListItem(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .sharedElement(
                                rememberSharedContentState(key = movie.backDropTransitionKey),
                                LocalNavAnimatedVisibilityScope.current,
                                boundsTransform = { _, _ ->
                                    tween(Constants.AnimationDurations.DEFAULT)
                                }),
                        resolveHeightOfItem(index),
                        movie,
                        onLoadSuccess = { movie.isLoaded.getContentIfNotHandled() },
                        onMovieClick = onMovieClick
                    )
                }
            }
        }

        if (isAppendLoading) {
            LinearProgressIndicator(
                Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun resolveHeightOfItem(positionOfItem: Int): Dp {
    val itemsHeight = arrayOf(
        dimensionResource(R.dimen.movie_image_height_type_short),
        dimensionResource(R.dimen.movie_image_height_type_long)
    )
    val itemViewType = when (positionOfItem % 8) {
        in 0..2 -> 0
        3 -> 1
        in 4..6 -> 0
        else -> 1
    }
    return itemsHeight[itemViewType]
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MoviesGridView() {

    PreviewSharingTransitionScreen { insetsPadding ->
        val context = LocalContext.current
        val pagingDataFlow = remember {
            val diContainer = DIContainer(context)
            val uiMapper = MovieUIMapper(diContainer.movieImageIdToUrlMapper)

            diContainer.previewMovieDataRepository
                .getTrendingMoviesInfo(1, 20)
                .map { movieInfo ->
                    PagingData.from(movieInfo.results.map {
                        uiMapper.fromDomain(it)

                    })
                }
        }
        MoviesStaggeredGridView(
            Modifier
                .fillMaxSize()
                .padding(insetsPadding),
            LocalAppState.current.numOfGridViewColumns,
            pagingDataFlow,
            onMovieClick = {}
        ) {}
    }
}