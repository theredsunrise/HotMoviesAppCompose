package com.example.hotmovies.presentation.shared.views

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hotmovies.presentation.shared.helpers.safeClickDecorator

@Composable
fun SafeClickableIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    IconButton(
        safeClickDecorator(onClick),
        modifier,
        isEnabled,
        colors,
        interactionSource,
        content
    )
}