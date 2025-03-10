package com.example.hotmovies.presentation.shared.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.hotmovies.R

@Composable
fun LottieFire(modifier: Modifier, isInverse: Boolean) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fire))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
    val color =
        if (isInverse) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color.hashCode(), BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "**"
            )
        )
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        dynamicProperties = dynamicProperties,
        progress = { progress },
    )
}