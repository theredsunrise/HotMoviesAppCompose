package com.example.hotmovies.presentation.shared.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Stable
@Immutable
interface CollapsibleHeaderConnectionInterface : NestedScrollConnection {
    val progress: Float
}

@Composable
fun rememberCollapsibleHeaderConnection(
    minHeight: Dp,
    maxHeight: Dp,
): CollapsibleHeaderConnectionInterface {

    val localDensity = LocalDensity.current
    val minPx = remember { with(localDensity) { minHeight.toPx() } }
    val maxPx = remember { with(localDensity) { maxHeight.toPx() } }
    val headerHeight = rememberSaveable { mutableFloatStateOf(maxPx) }

    return remember {
        object : CollapsibleHeaderConnectionInterface {
            override val progress: Float
                get() = 1f - (headerHeight.value - minPx) / (maxPx - minPx);

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val height = headerHeight.value;

                if (height + available.y > maxPx) {
                    headerHeight.value = maxPx
                    return Offset(available.x, maxPx - height)
                }

                if (height + available.y < minPx) {
                    headerHeight.value = minPx
                    return Offset(available.x, minPx - height)
                }

                headerHeight.value += available.y
                return Offset(available.x, available.y)
            }
        }
    }
}