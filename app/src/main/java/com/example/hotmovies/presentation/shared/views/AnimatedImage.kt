package com.example.hotmovies.presentation.shared.views


import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hotmovies.R
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme
import com.example.hotmovies.shared.Constants
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.stateFailure

data class CustomPainter(val painter: Painter, val isCached: Boolean)

@Composable
fun AnimatedImage(
    modifier: Modifier,
    imageState: State<ResultState<CustomPainter>>,
    animationDuration: Int = Constants.AnimationDurations.DEFAULT,
) {
    val revealFraction = remember { Animatable(0f) }
    LaunchedEffect(imageState.value) {
        val imageState = imageState.value
        when (imageState.success?.isCached) {
            true -> revealFraction.snapTo(1f)
            false -> revealFraction.animateTo(1f, animationSpec = tween(animationDuration))
            else -> revealFraction.snapTo(0f)
        }
    }

    Box(
        modifier.background(
            MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        when (val imageState = imageState.value) {
            is ResultState.Success ->
                Image(
                    imageState.value.painter, "Thumbnail",
                    Modifier
                        .matchParentSize()
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                        .drawWithCache {
                            val path = Path().apply {
                                addOval(
                                    Rect(
                                        Offset.Zero,
                                        1.5f * size.maxDimension * revealFraction.value
                                    )
                                )
                            }
                            onDrawWithContent {
                                clipPath(path) {
                                    this@onDrawWithContent.drawContent()
                                }

                            }
                        },
                    contentScale = ContentScale.Crop,
                )

            is ResultState.Failure -> {
                Icon(
                    ImageVector.vectorResource(R.drawable.vector_cross),
                    "Failure Icon",
                    Modifier
                        .requiredSize(30.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            is ResultState.Progress, is ResultState.None ->
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .requiredSize(30.dp, 30.dp)
                        .align(Alignment.Center),
                    trackColor = Color.Transparent
                )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = "spec:parent=pixel_5")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAnimatedImage() {
    HotMoviesAppComposeTheme {
        //val painter =
        //    rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYx92dByyPkxP3kLeTs8mr9E15heeWh5ODzomNBXJwyaMDXlHp")
        //AnimatedImage(Modifier.requiredSize(390.dp), painter.rememberAnimatedImageState(), 4000)

        val painter = painterResource(R.drawable.spongebob)
        AnimatedImage(
            Modifier.requiredSize(390.dp),
            remember { mutableStateOf(Exception("").stateFailure()) },
            4000
        )


    }
}