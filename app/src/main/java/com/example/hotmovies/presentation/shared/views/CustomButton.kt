package com.example.hotmovies.presentation.shared.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme

@Stable
@Immutable
data class CustomButtonConfigurator(
    val isEnabled: Boolean = true,
    val isAnimated: Boolean = false,
    val percentCorners: Int = 100,
    val animatedProgress: Float = 0f
)

@Composable
fun CustomButton(
    modifier: Modifier,
    configuratorClosure: () -> CustomButtonConfigurator,
    title: String,
    onClick: () -> Unit
) {
    val configurator = configuratorClosure()
    val animationContainerColor = MaterialTheme.colorScheme.secondaryContainer

    Button(
        onClick,
        modifier.drawWithCache {
            onDrawWithContent {
                if (configurator.isAnimated) {
                    drawRoundRect(
                        color = animationContainerColor,
                        cornerRadius = CornerRadius(
                            lerp(
                                size.height * 0.01f * configurator.percentCorners,
                                size.height,
                                configurator.animatedProgress
                            )
                        )
                    )
                } else {
                    this@onDrawWithContent.drawContent()
                }
            }
        },
        enabled = configurator.isEnabled && !configurator.isAnimated,
        border = null,
        shape = RoundedCornerShape(percent = configurator.percentCorners),
        contentPadding = PaddingValues(20.dp)
    ) {
        Text(if (configurator.isAnimated) "" else title)
    }
}

@Preview
@Composable
private fun PreviewCustomButton() {
    HotMoviesAppComposeTheme {
        val configurator =
            CustomButtonConfigurator(
            )
        CustomButton(Modifier.fillMaxWidth(), { configurator }, "Text") {}
    }
}
