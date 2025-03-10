package com.example.hotmovies.presentation.login.initialization

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hotmovies.presentation.shared.views.CustomCircularProgressIndicator
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.checkMainThread
import com.example.hotmovies.shared.progressEvent


@Composable
fun InitializationScreen(
    sessionValidityState: State<Event<ResultState<Boolean>>>,
    onSessionValid: (isValid: Boolean) -> Unit,
    onError: (exception: Exception) -> Unit
) {

    LaunchedEffect(sessionValidityState.value) {
        checkMainThread()

        val sessionValidityEvent = sessionValidityState.value
        if (sessionValidityEvent.content is ResultState.Failure) {
            onError(sessionValidityEvent.content.exception)
            return@LaunchedEffect
        }

        val sessionValidityAction =
            sessionValidityEvent.getContentIfNotHandled() ?: return@LaunchedEffect
        when {
            sessionValidityAction is ResultState.Success -> onSessionValid(
                sessionValidityAction.value
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (sessionValidityState.value.content.isProgress) {
            CustomCircularProgressIndicator(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                isInverse = false
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewInitializationScreen() {
    HotMoviesAppComposeTheme {
        Surface() { ->
            InitializationScreen(
                remember { mutableStateOf(progressEvent) },
                onSessionValid = {},
                onError = {})
        }
    }
}