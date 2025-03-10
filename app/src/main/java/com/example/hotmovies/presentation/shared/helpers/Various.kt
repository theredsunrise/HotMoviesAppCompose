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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.hotmovies.presentation.shared.LocalNavAnimatedVisibilityScope
import com.example.hotmovies.presentation.shared.LocalSharedTransitionScope
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme

@Composable
fun safeClickDecorator(onClick: () -> Unit): () -> Unit {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var timeStamp by remember { mutableLongStateOf(System.currentTimeMillis()) }
    return {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            val now = System.currentTimeMillis()
            if (now - timeStamp >= 300) {
                onClick()
            }
            timeStamp = now
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
