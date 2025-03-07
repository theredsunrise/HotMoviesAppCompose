package com.example.hotmovies.presentation.shared.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.END
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.PROGRESS
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.START

enum class FloatFractionAnimationState {
    START,
    PROGRESS,
    END
}

@Composable
fun FloatFractionAnimationStates(
    animatedValue: State<Float>,
    onChange: ((old: FloatFractionAnimationState, new: FloatFractionAnimationState) -> Unit)? = null
): State<FloatFractionAnimationState> {
    return FloatFractionAnimationStates(animatedValue.value, onChange)
}

@Composable
fun FloatFractionAnimationStates(
    animatedValue: Float,
    onChange: ((old: FloatFractionAnimationState, new: FloatFractionAnimationState) -> Unit)? = null
): State<FloatFractionAnimationState> {

    val statesValue = when (animatedValue) {
        0f -> START
        1f -> END
        else -> PROGRESS
    }
    val states = remember(statesValue) { mutableStateOf(statesValue) }
    var oldValue by remember { mutableStateOf(START) }

    LaunchedEffect(states.value) {
        val newValue = states.value
        onChange?.invoke(oldValue, newValue)
        when (newValue) {
            START, END -> oldValue = newValue
            else -> {}
        }
    }
    return states
}