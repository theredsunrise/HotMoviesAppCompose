package com.example.hotmovies.presentation.login.login

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcAnimationSpec
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hotmovies.R
import com.example.hotmovies.presentation.login.viewModel.actions.LoginViewModel
import com.example.hotmovies.presentation.login.viewModel.actions.LoginViewModel.Actions.Animation
import com.example.hotmovies.presentation.login.viewModel.actions.LoginViewModel.Actions.Login
import com.example.hotmovies.presentation.login.viewModel.actions.LoginViewModel.Actions.UpdatePassword
import com.example.hotmovies.presentation.login.viewModel.actions.LoginViewModel.Actions.UpdateUserName
import com.example.hotmovies.presentation.shared.LocalNavAnimatedVisibilityScope
import com.example.hotmovies.presentation.shared.UIControlState
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.END
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.PROGRESS
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationState.START
import com.example.hotmovies.presentation.shared.helpers.FloatFractionAnimationStates
import com.example.hotmovies.presentation.shared.helpers.LifecycleAwareFlowsCollector
import com.example.hotmovies.presentation.shared.helpers.PreviewSharingTransitionScreen
import com.example.hotmovies.presentation.shared.isEnabled
import com.example.hotmovies.presentation.shared.views.CustomButton
import com.example.hotmovies.presentation.shared.views.CustomButtonConfigurator
import com.example.hotmovies.presentation.shared.views.CustomCircularProgressIndicator
import com.example.hotmovies.presentation.shared.views.CustomTextField
import com.example.hotmovies.presentation.shared.views.CustomTextFieldConfigurator
import com.example.hotmovies.presentation.shared.views.LottieFire
import com.example.hotmovies.presentation.theme.colorPrimaryDarkerMiddle
import com.example.hotmovies.presentation.theme.lobsterFontFamily
import com.example.hotmovies.shared.Constants
import com.example.hotmovies.shared.checkMainThread
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@OptIn(
    ExperimentalMotionApi::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationSpecApi::class
)
@Composable
fun SharedTransitionScope.LoginScreen(
    modifier: Modifier,
    usernameText: State<String>,
    passwordText: State<String>,
    loginFlow: StateFlow<LoginViewModel.UIState>,
    onAction: (LoginViewModel.Actions) -> Unit,
    onLogin: () -> Unit
) {

    val currentActivity = LocalActivity.current
    val focusManager = LocalFocusManager.current
    val loginState = loginFlow.collectAsStateWithLifecycle()

    var launchAnimation by remember { mutableStateOf(false) }
    val animate = animateFloatAsState(
        if (launchAnimation) 1f else 0f,
        label = "Animation",
        animationSpec = tween(Constants.AnimationDurations.DEFAULT)
    )

    LifecycleAwareFlowsCollector(loginFlow) { value ->
        checkMainThread()

        currentActivity?.isEnabled = value.isScreenEnabled
        val loginAction =
            value.loginAction.getContentIfNotHandled() ?: return@LifecycleAwareFlowsCollector
        when {
            loginAction.isSuccessTrue -> onLogin()
            loginAction.isFailure -> launchAnimation = false
        }
    }

    MotionLayout(
        LoginMotionScene(),
        progress = animate.value,
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {

        FloatFractionAnimationStates(animate) { oldState, newState ->
            onAction(Animation(newState == PROGRESS))
            if (oldState == START && newState == END) {
                onAction(Login)
            }
        }

        LottieFire(Modifier.layoutId("fireStart"), true)

        LottieFire(Modifier.layoutId("fireEnd"), true)

        CustomTextField(
            Modifier.layoutId("username"),
            {
                CustomTextFieldConfigurator(
                    isEnabled = loginState.value.isScreenEnabled,
                    error = loginState.value.userNameText.exception?.localizedMessage,
                    isAnimated = animate.value != 0f,
                    animatedProgress = animate.value
                )
            },
            stringResource(R.string.userName),
            usernameText,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        ) {
            onAction(UpdateUserName(it))
        }

        CustomTextField(
            modifier = Modifier.layoutId("password"),
            {
                CustomTextFieldConfigurator(
                    isEnabled = loginState.value.isScreenEnabled,
                    error = loginState.value.passwordText.exception?.localizedMessage,
                    isPassword = true,
                    isAnimated = animate.value != 0f,
                    animatedProgress = animate.value
                )
            },
            stringResource(R.string.password),
            passwordText,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                launchAnimation = true
            })
        ) { onAction(UpdatePassword(it)) }

        Text(
            stringResource(R.string.app_name),
            modifier = Modifier.layoutId("appTitle"),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.displayLarge,
            fontFamily = if (LocalInspectionMode.current) null else lobsterFontFamily()
        )

        Text(
            stringResource(R.string.app_description),
            modifier = Modifier.layoutId("description"),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            stringResource(R.string.credits_and_attributions),
            modifier = Modifier.layoutId("credits"),
            textAlign = TextAlign.Center,
            color = colorPrimaryDarkerMiddle,
            style = MaterialTheme.typography.labelSmall
        )

        //PREVIEW
        if (LocalInspectionMode.current) {
            CustomButton(
                modifier = Modifier
                    .layoutId("login"),
                {
                    CustomButtonConfigurator(
                        isEnabled = loginState.value.let { it.isScreenEnabled && it.loginButton.isEnabled },
                        isAnimated = animate.value != 0f,
                        animatedProgress = animate.value,
                    )
                },
                "Login",
            ) { launchAnimation = true }
        } else {
            CustomButton(
                modifier = Modifier
                    .layoutId("login")
                    .sharedBounds(
                        rememberSharedContentState(key = "Transition"),
                        LocalNavAnimatedVisibilityScope.current,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        boundsTransform = { _, _ ->
                            ArcAnimationSpec(
                                ArcMode.ArcBelow, Constants.AnimationDurations.DEFAULT
                            )
                        }),
                {
                    CustomButtonConfigurator(
                        isEnabled = loginState.value.let { it.isScreenEnabled && it.loginButton.isEnabled },
                        isAnimated = animate.value != 0f,
                        animatedProgress = animate.value,
                    )
                },
                stringResource(R.string.login),
            ) { launchAnimation = true }
        }

        CustomCircularProgressIndicator(
            modifier = Modifier.layoutId("indicator"),
            isInverse = true
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, DelicateCoroutinesApi::class)
@Preview(
    showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:parent=pixel_5"
)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenPreview() {
    PreviewSharingTransitionScreen {
        val scope = rememberCoroutineScope()
        val state = LoginViewModel.UIState.defaultState().copy(
            loginButton = UIControlState(
                isEnabled = true,
                exception = null
            )
        )
        LoginScreen(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeContent),
            remember { mutableStateOf("Test") },
            remember { mutableStateOf("Password123456") },
            remember {
                flowOf(
                    state
                ).stateIn(
                    scope,
                    SharingStarted.WhileSubscribed(8000),
                    state
                )
            },
            onAction = {},
            onLogin = {})
    }

}