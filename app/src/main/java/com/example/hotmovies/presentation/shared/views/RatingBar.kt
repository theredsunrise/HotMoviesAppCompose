package com.example.hotmovies.presentation.shared.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme
import kotlin.math.ceil

@Composable
fun RatingBar(
    modifier: Modifier,
    numOfStars: Int = 5,
    rating: Float,
    inverse: Boolean
) {
    val innerStarScaleFactor = 0.80f
    val outerStarScaleFactor = 1.2f

    check(numOfStars >= 1)
    check((0f..numOfStars.toFloat()).contains(rating))
    Box(modifier) {
        val colors = arrayOf(
            MaterialTheme.colorScheme.onPrimaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
        Row(
            Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { starNumber ->
                val starNumberFloat = starNumber.toFloat()
                Box(Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Star, contentDescription = "Star",
                        modifier = Modifier
                            .wrapContentSize()
                            .graphicsLayer {
                                scaleX = outerStarScaleFactor
                                scaleY = outerStarScaleFactor
                            }
                            .aspectRatio(1f, true),
                        tint = if (inverse) colors[1] else colors[0]
                    )
                    Icon(
                        imageVector = Icons.Default.Star, contentDescription = "Star",
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = innerStarScaleFactor
                                scaleY = innerStarScaleFactor
                            }
                            .wrapContentHeight()
                            .aspectRatio(1f, true),
                        tint = if (inverse) colors[0] else colors[1]
                    )

                    val fraction = when {
                        starNumberFloat <= rating -> 1f
                        ceil(rating) == starNumberFloat -> rating - (starNumberFloat - 1)
                        else -> 0f
                    }
                    Box(
                        Modifier
                            .wrapContentSize()
                            .graphicsLayer(clip = true, shape = FractionalClipShape(fraction))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star, contentDescription = "Star",
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = outerStarScaleFactor
                                    scaleY = outerStarScaleFactor
                                }
                                .wrapContentHeight()
                                .aspectRatio(1f, true),
                            tint = if (inverse) colors[0] else colors[1]
                        )
                        Icon(
                            imageVector = Icons.Default.Star, contentDescription = "Star",
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = innerStarScaleFactor
                                    scaleY = innerStarScaleFactor
                                }
                                .wrapContentHeight()
                                .aspectRatio(1f, true),
                            tint = if (inverse) colors[1] else colors[0]
                        )
                    }
                }
            }
        }
    }
}

private class FractionalClipShape(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = "spec:parent=pixel_5")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewRatingBar() {
    HotMoviesAppComposeTheme {
        RatingBar(Modifier.requiredSize(200.dp, 40.dp), 5, 2.5f, inverse = false)
    }
}