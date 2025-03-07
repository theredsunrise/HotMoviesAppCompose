package com.example.hotmovies.presentation.shared.views

import android.content.res.Configuration
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hotmovies.R
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme


@Stable
@Immutable
data class CustomDialogState(val id: Any, val message: String)

@Composable
fun CustomDialog(
    state: MutableState<CustomDialogState?>,
    @StringRes confirmStringId: Int,
    @StringRes titleStringId: Int = R.string.dialog_info_title,
    onDismissRequest: (() -> Unit)? = null,
    onCancel: (Any) -> Unit = {},
    onConfirm: (Any) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest?.invoke()
            state.value = null
        }, properties = DialogProperties(
            dismissOnClickOutside = onDismissRequest != null,
            dismissOnBackPress = onDismissRequest != null,
            decorFitsSystemWindows = false,
            usePlatformDefaultWidth = true
        )
    ) {

        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        LaunchedEffect(true) {
            dialogWindowProvider.window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setDimAmount(0.5f)
            }
        }

        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                iconLottie(modifier = Modifier.size(130.dp, 130.dp))

                Text(
                    text = stringResource(titleStringId),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
                )

                Text(
                    text = state.value?.message.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp)
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors()
                            .copy(contentColor = MaterialTheme.colorScheme.inverseOnSurface),
                        onClick = {
                            state.value?.id?.also {
                                onCancel(it)
                            }
                            state.value = null
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .padding(start = 16.dp),
                    ) {
                        Text(
                            style = MaterialTheme.typography.headlineMedium,
                            text = stringResource(android.R.string.cancel)
                        )
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        onClick = {
                            state.value?.id?.also {
                                onConfirm(it)
                            }
                            state.value = null
                        },
                        modifier = Modifier
                            .defaultMinSize(minWidth = 140.dp)
                            .padding(8.dp)
                            .padding(end = 16.dp),
                    ) {
                        Text(
                            stringResource(confirmStringId),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun iconLottie(modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier = modifier,
        contentScale = ContentScale.FillHeight,
        composition = composition,
        progress = { progress },
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCustomDialog() {
    HotMoviesAppComposeTheme {
        CustomDialog(
            remember {
                mutableStateOf(
                    CustomDialogState(
                        0,
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry" +
                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry"
                    )
                )
            },
            confirmStringId = R.string.dialog_action_retry
        ) {

        }
    }
}