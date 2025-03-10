package com.example.hotmovies.presentation.shared.views

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun CustomCircularProgressIndicator(modifier: Modifier, isInverse: Boolean, isVisible: Boolean = true) =
    CircularProgressIndicator(
        strokeWidth = 3.dp,
        color = if (isInverse) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary,
        modifier = modifier
            .graphicsLayer {
                alpha =
                    if (isVisible) 1f else 0f
            },
        trackColor = Color.Transparent
    )