package com.example.hotmovies.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.hotmovies.R

object CustomFonts {
    val lobsterFont = Font(R.font.lobster)
    val lobsterFontFamily = FontFamily(lobsterFont)
}

@Composable
@ReadOnlyComposable
fun lobsterFontFamily(): FontFamily {
    return CustomFonts.lobsterFontFamily
}
