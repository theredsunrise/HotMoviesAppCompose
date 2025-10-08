package com.example.hotmovies.presentation.shared.helpers

import android.annotation.SuppressLint
import android.os.Build
import android.view.WindowInsets
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.example.hotmovies.presentation.shared.LocalNavAnimatedVisibilityScope
import com.example.hotmovies.presentation.shared.LocalSharedTransitionScope
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme
import kotlin.time.ExperimentalTime

@Composable
@OptIn(ExperimentalTime::class)
fun safeClickDecorator(throttleDelayMs: Long = 300L, onClick: () -> Unit): () -> Unit {
    val onClickState = rememberUpdatedState(onClick)
    return remember(throttleDelayMs) {
        var timeStamp = 0L
        {
            val now: Long = System.currentTimeMillis()
            if (now - timeStamp >= throttleDelayMs) {
                onClickState.value()
                timeStamp = now
            }
        }
    }
}

@Suppress("DEPRECATION")
@SuppressLint("NewApi")
@Composable
fun isBackGestureEnabled(): Boolean {
    val version = Build.VERSION.SDK_INT
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) return false

    return with(LocalView.current.rootWindowInsets) {
        (if (version <= Build.VERSION_CODES.Q) systemGestureInsets
        else getInsets(WindowInsets.Type.systemGestures())).left > 0
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewSharingTransitionScreen(content: @Composable SharedTransitionScope.(PaddingValues) -> Unit) {
    HotMoviesAppComposeTheme {
        Scaffold(Modifier.windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.safeDrawing)) { insetsPadding ->
            SharedTransitionScope {
                CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                    AnimatedContent(
                        0,
                        Modifier
                            .fillMaxSize()
                            .padding(insetsPadding)
                    ) { _ ->
                        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this@AnimatedContent) {
                            content(insetsPadding)
                        }
                    }
                }
            }
        }
    }
}
