package com.example.hotmovies.presentation.movies.list

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcAnimationSpec
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.example.hotmovies.R
import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.presentation.movies.dtos.MovieUI
import com.example.hotmovies.presentation.movies.dtos.MovieUIMapper
import com.example.hotmovies.presentation.movies.dtos.pagingDataProgress
import com.example.hotmovies.presentation.movies.list.uiStateProcessors.ProcessUserDetails
import com.example.hotmovies.presentation.movies.list.uiStateProcessors.ProcessUserLogout
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel
import com.example.hotmovies.presentation.movies.list.views.MoviesStaggeredGridView
import com.example.hotmovies.presentation.shared.LocalAppState
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.END
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.PROGRESS
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationStates
import com.example.hotmovies.presentation.shared.helpers.rememberCollapsibleHeaderConnection
import com.example.hotmovies.presentation.shared.isEnabled
import com.example.hotmovies.presentation.shared.lobsterFontFamily
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme
import com.example.hotmovies.shared.Constants
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.math.min


@Stable
@Immutable
sealed interface MoviesScreenErrorSource {
    data object LogoutError : MoviesScreenErrorSource
    data object LoadUserDetailsError : MoviesScreenErrorSource
    data object LoadMoviesError : MoviesScreenErrorSource
}

@OptIn(
    ExperimentalMotionApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationSpecApi::class
)
@Composable
fun MoviesScreen(
    modifier: Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    moviesFlow: StateFlow<MoviesViewModel.UIState>,
    moviesPagingDataFlow: StateFlow<PagingData<MovieUI>>,
    onAction: (MoviesViewModel.Actions) -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onError: (source: MoviesScreenErrorSource, exception: Exception) -> Unit
) {

    val currentActivity = LocalActivity.current
    val minHeaderHeight = dimensionResource(R.dimen.min_height_collapsible_header)
    val maxHeaderHeight = dimensionResource(R.dimen.max_height_collapsible_header)
    val moviesInfoCutCorner = dimensionResource(R.dimen.movies_info_cut_corner)

    val moviesState = moviesFlow.collectAsStateWithLifecycle()

    val transitionOverlayFractionProgress = animatedContentScope.transition.animateFloat(label = "",
        transitionSpec = {
            if (EnterExitState.Visible isTransitioningTo EnterExitState.PostExit)
                SnapSpec()
            else
                tween(Constants.AnimationDurations.DEFAULT)
        }) { state ->
        when (state) {
            EnterExitState.PreEnter -> 0f
            EnterExitState.Visible -> 1f
            EnterExitState.PostExit -> 1f
        }
    }
    val transitionOverlayFractionProgressState by FloatFractionAnimationStates(
        transitionOverlayFractionProgress
    ) { oldState, newState ->
        currentActivity?.isEnabled = newState != PROGRESS
    }
    val headerScrollConnection =
        rememberCollapsibleHeaderConnection(minHeaderHeight, maxHeaderHeight)

    BackHandler(true) {
        onBack()
    }

    if (transitionOverlayFractionProgressState == END) {
        ProcessUserDetails(
            remember { moviesFlow.map { it.userDetailsAction } },
            onAction,
            onError
        )
        ProcessUserLogout(
            currentActivity,
            remember { moviesFlow.map { it.logoutAction } },
            onLogout,
            onError
        )
    }

    MotionLayout(
        MoviesMotionScene(LocalAppState.current.compactCollapsibleHeader, minHeaderHeight.value),
        progress = headerScrollConnection.progress,
        modifier = modifier
    ) {

        //PREVIEW
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .layoutId("info")
                    .clip(
                        if (transitionOverlayFractionProgressState == END) {
                            CutCornerShape(
                                topStart = lerp(
                                    moviesInfoCutCorner, 0.dp,
                                    min(1f, 1.3f * headerScrollConnection.progress)
                                )
                            )
                        } else {
                            RectangleShape
                        }
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
        } else {
            with(sharedTransitionScope) {
                Box(
                    modifier = Modifier
                        .layoutId("info")
                        .clip(
                            if (transitionOverlayFractionProgressState == END) {
                                CutCornerShape(
                                    topStart = lerp(
                                        moviesInfoCutCorner, 0.dp,
                                        min(1f, 1.3f * headerScrollConnection.progress)
                                    )
                                )
                            } else {
                                RectangleShape
                            }
                        )
                        .sharedBounds(
                            rememberSharedContentState(key = "Transition"),
                            animatedContentScope,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            boundsTransform = { _, _ ->
                                ArcAnimationSpec(
                                    ArcMode.ArcBelow,
                                    Constants.AnimationDurations.DEFAULT
                                )
                            },
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            clipInOverlayDuringTransition = moviesTransitionOverlayClip(
                                moviesInfoCutCorner,
                                transitionOverlayFractionProgress.value
                            )
                        )
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.vector_background),
            contentDescription = "background",
            contentScale = ContentScale.Fit,
            alignment = Alignment.TopStart,
            modifier = Modifier.layoutId("background")
        )

        //PREVIEW
        if (LocalInspectionMode.current) {
            Image(
                bitmap = moviesState.value.userDetails.avatar ?: ImageBitmap(1, 1),
                contentDescription = "avatar",
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopStart,
                modifier = Modifier
                    .layoutId("avatar")
                    .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surface)
                    .drawWithContent {
                        if (!moviesState.value.userDetailsAction.content.isProgress) {
                            drawContent()
                        }
                    }
            )
        } else {
            with(sharedTransitionScope) {
                Image(
                    bitmap = moviesState.value.userDetails.avatar ?: ImageBitmap(1, 1),
                    contentDescription = "avatar",
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.TopStart,
                    modifier = Modifier
                        .layoutId("avatar")
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 1f
                        )
                        .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surface)
                        .drawWithContent {
                            if (!moviesState.value.userDetailsAction.content.isProgress) {
                                drawContent()
                            }
                        }
                )
            }
        }

        CircularProgressIndicator(
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .layoutId("indicator")
                .graphicsLayer {
                    alpha =
                        if (moviesState.value.userDetailsAction.content.isProgress) 1f else 0f
                },
            trackColor = Color.Transparent
        )

        //PREVIEW
        if (LocalInspectionMode.current) {
            Text(
                moviesState.value.userDetails.name,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .layoutId("name")
                    .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                moviesState.value.userDetails.userName,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .layoutId("username")
                    .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                moviesState.value.userDetails.overview,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .layoutId("description")
                    .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        } else {
            with(sharedTransitionScope) {
                Text(
                    moviesState.value.userDetails.name,
                    style = MaterialTheme.typography.displayMedium,
                    fontFamily = lobsterFontFamily(),
                    modifier = Modifier
                        .layoutId("name")
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 2f
                        )
                        .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    moviesState.value.userDetails.userName,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .layoutId("username")
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 2f
                        )
                        .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    moviesState.value.userDetails.overview,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .layoutId("description")
                        .renderInSharedTransitionScopeOverlay(
                            zIndexInOverlay = 2f
                        )
                        .alpha(if (transitionOverlayFractionProgressState == END) 1f else 0f),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        val showLoading =
            with(moviesState.value.logoutAction.content) { isSuccessTrue || isProgress || isFailure }
        MoviesStaggeredGridView(
            modifier = Modifier
                .layoutId("moviesGrid")
                .background(MaterialTheme.colorScheme.surface)
                .nestedScroll(headerScrollConnection),
            visible = transitionOverlayFractionProgressState == END,
            numOfColumns = LocalAppState.current.numOfGridViewColumns,
            if (showLoading) {
                remember { flowOf(pagingDataProgress) }
            } else {
                moviesPagingDataFlow
            }
        ) { onError(MoviesScreenErrorSource.LoadMoviesError, it) }
    }
}

@SuppressLint("FlowOperatorInvokedInComposition")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=1344px,height=2992px,dpi=480,cutout=double"
)
@Composable
private fun MoviesScreenPreview() {
    HotMoviesAppComposeTheme {
        Scaffold(Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) { insetsPadding ->
            SharedTransitionScope {
                AnimatedContent(
                    0,
                    Modifier
                        .fillMaxSize()
                        .padding(insetsPadding)
                ) { _ ->

                    val scope = rememberCoroutineScope()
                    val state = MoviesViewModel.UIState.defaultState()
                    val stateFlow = remember {
                        flowOf(
                            state
                        ).stateIn(scope, SharingStarted.WhileSubscribed(8000), state)
                    }

                    val context = LocalContext.current
                    val pagingDataFlow = remember {
                        val diContainer = DIContainer(context)
                        val uiMapper = MovieUIMapper(diContainer.tmdbMovieImageIdToUrlMapper)

                        diContainer.previewMovieDataRepository
                            .getTrendingMoviesInfo(1, 20)
                            .map { movieInfo ->
                                PagingData.from(movieInfo.results.map {
                                    uiMapper.fromDomain(it)

                                })
                            }
                            .stateIn(
                                scope,
                                SharingStarted.WhileSubscribed(5000),
                                pagingDataProgress
                            )
                    }

                    MoviesScreen(
                        Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars.union(WindowInsets.displayCutout)),
                        this@SharedTransitionScope,
                        this@AnimatedContent,
                        stateFlow,
                        pagingDataFlow,
                        onAction = {},
                        onBack = {},
                        onLogout = {}
                    ) { _, _ -> }
                }
            }
        }
    }
}


