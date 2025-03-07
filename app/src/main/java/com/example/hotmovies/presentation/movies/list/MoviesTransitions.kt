package com.example.hotmovies.presentation.movies.list

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.moviesTransitionOverlayClip(
    topStartCutCorner: Dp,
    fractionProgress: Float
) = OverlayClip(
    if (fractionProgress < 0.5f) {
        val percent = lerp(100, 0, 2f * fractionProgress)
        RoundedCornerShape(percent = percent)
    } else {
        val topStart =
            lerp(0.dp, topStartCutCorner, 2f * (fractionProgress - 0.5f))
        CutCornerShape(topStart = topStart)
    }
)
