package com.example.hotmovies.presentation.shared

import android.content.res.Configuration
import androidx.compose.runtime.compositionLocalOf
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

data class AppState(
    val compactCollapsibleHeader: Boolean = false,
    val numOfGridViewColumns: Int = 2
) {
    companion object {
        fun createFrom(configuration: Configuration, windowSizeClass: WindowSizeClass): AppState {
            val compactCollapsibleHeader =
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT
            val numOfGridViewColumns = when (windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.COMPACT ->  {
                    when(configuration.screenWidthDp) {
                        in 0..390 -> 1
                        else -> 2
                    }
                }
                WindowWidthSizeClass.MEDIUM, WindowWidthSizeClass.EXPANDED -> {
                    when(configuration.screenWidthDp) {
                        in 0..1000 -> 3
                        else -> 4
                    }
                }
                else -> 1
            }
            return AppState(compactCollapsibleHeader, numOfGridViewColumns)
        }
    }
}

val LocalAppState = compositionLocalOf { AppState() }