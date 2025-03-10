package com.example.hotmovies.presentation.theme

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import com.example.hotmovies.presentation.shared.AppState
import com.example.hotmovies.presentation.shared.LocalAppState
import com.example.hotmovies.presentation.shared.helpers.isBackGestureEnabled

private val LightColorsScheme = lightColorScheme(
    primary = md_theme_light_primary,
    inversePrimary = md_theme_light_inversePrimary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurface = md_theme_light_onSurface,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    surfaceContainer = md_theme_light_surfaceContainer,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    outline = md_theme_light_outline
)

private val DarkColorsScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    inversePrimary = md_theme_dark_inversePrimary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurface = md_theme_dark_onSurface,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    surfaceContainer = md_theme_dark_surfaceContainer,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    outline = md_theme_dark_outline
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HotMoviesAppComposeTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        isDarkTheme -> DarkColorsScheme
        else -> LightColorsScheme
    }

    val configuration = LocalConfiguration.current
    val isBackGestureEnabled = isBackGestureEnabled()
    CompositionLocalProvider(
        LocalAppState provides AppState.createFrom(
            configuration,
            currentWindowAdaptiveInfo().windowSizeClass,
            isBackGestureEnabled
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

