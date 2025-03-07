package com.example.hotmovies.presentation.shared

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.decode.DataSource
import com.example.hotmovies.R
import com.example.hotmovies.UserInteractionConfigurableComponent
import com.example.hotmovies.presentation.shared.views.CustomDialog
import com.example.hotmovies.presentation.shared.views.CustomDialogState
import com.example.hotmovies.presentation.shared.views.CustomPainter
import com.example.hotmovies.presentation.theme.CustomFonts
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.none
import com.example.hotmovies.shared.progress
import com.example.hotmovies.shared.state
import com.example.hotmovies.shared.stateFailure

//@Composable
//fun Activity.toString(@StringRes stringId: Int): String = remember { getString(stringId) }
//
//@Composable
//fun Context.toString(@StringRes stringId: Int): String = remember { getString(stringId) }

@Composable
fun showDialog(
    @StringRes confirmStringId: Int = R.string.dialog_action_retry,
    @StringRes titleStringId: Int = R.string.dialog_info_title,
    onCancel: (Any) -> Unit = {}, onConfirm: (Any) -> Unit
): MutableState<CustomDialogState?> {
    val dialogState = remember { mutableStateOf<CustomDialogState?>(null) }
    dialogState.value?.also {
        CustomDialog(
            dialogState,
            confirmStringId,
            titleStringId,
            onCancel = onCancel,
            onConfirm = onConfirm
        )
    }
    return dialogState
}

@Composable
fun AsyncImagePainter.rememberAnimatedImageState(): State<ResultState<CustomPainter>> {
    val coilPainterState by this.state.collectAsStateWithLifecycle()
    return remember {
        derivedStateOf {
            when (val painterState = coilPainterState) {
                is AsyncImagePainter.State.Empty -> {
                    none
                }

                is AsyncImagePainter.State.Loading -> {
                    progress
                }

                is AsyncImagePainter.State.Success -> {
                    with(painterState) {
                        CustomPainter(
                            painter,
                            result.dataSource != DataSource.NETWORK
                        ).state()
                    }
                }

                is AsyncImagePainter.State.Error -> {
                    Exception(painterState.result.throwable).stateFailure()
                }
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun lobsterFontFamily(): FontFamily {
    return CustomFonts.lobsterFontFamily
}

fun <V : View> Set<V>.toPairs(): Array<Pair<V, String>> {
    return map { Pair(it, it.transitionName) }.toTypedArray()
}

val Activity.userInteractionComponent: UserInteractionConfigurableComponent
    get() = this as UserInteractionConfigurableComponent

var Activity.isEnabled: Boolean
    get() = userInteractionComponent.isEnabled
    set(value) {
        userInteractionComponent.isEnabled = value
    }