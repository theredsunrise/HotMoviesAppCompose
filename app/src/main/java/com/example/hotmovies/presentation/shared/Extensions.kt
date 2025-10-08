package com.example.hotmovies.presentation.shared

import android.app.Activity
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.decode.DataSource
import com.example.hotmovies.UserInteractionConfigurableComponent
import com.example.hotmovies.presentation.shared.helpers.safeClickDecorator
import com.example.hotmovies.presentation.shared.views.CustomPainter
import com.example.hotmovies.shared.Constants
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.none
import com.example.hotmovies.shared.progress
import com.example.hotmovies.shared.state
import com.example.hotmovies.shared.stateFailure

@Composable
fun AsyncImagePainter.rememberAnimatedImageState(
    isAlwaysAnimated: Boolean = false,
    onSuccess: () -> Unit
): State<ResultState<CustomPainter>> {
    val coilPainterState by this.state.collectAsStateWithLifecycle()
    val onSuccessState by rememberUpdatedState(onSuccess)

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
                    onSuccessState()
                    with(painterState) {
                        CustomPainter(
                            painter,
                            isAlwaysAnimated || (result.dataSource == DataSource.NETWORK)
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
fun Modifier.safeClickable(
    isEnabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier {

    val clickableModifier = Modifier.clickable(
        isEnabled,
        onClickLabel,
        role,
        safeClickDecorator(onClick = onClick)
    )
    return this then clickableModifier
}


@Composable
fun Transition<EnterExitState>.floatFractionTransitionStates(): State<Float> =
    animateFloat(
        label = "Transition Fraction Animation",
        transitionSpec = {
            if (EnterExitState.Visible isTransitioningTo EnterExitState.PostExit)
                SnapSpec()
            else
                tween(Constants.AnimationDurations.DEFAULT)
        }) { state ->
        when (state) {
            EnterExitState.PreEnter -> 0f
            EnterExitState.Visible -> 1f
            EnterExitState.PostExit -> 1f
        }
    }

val Activity.userInteractionComponent: UserInteractionConfigurableComponent
    get() = this as UserInteractionConfigurableComponent

var Activity.isEnabled: Boolean
    get() = userInteractionComponent.isEnabled
    set(value) {
        userInteractionComponent.isEnabled = value
    }

