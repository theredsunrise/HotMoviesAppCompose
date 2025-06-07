package com.example.hotmovies.presentation.login.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.hotmovies.CustomApplication
import com.example.hotmovies.presentation.login.initialization.InitializationScreen
import com.example.hotmovies.presentation.login.initialization.viewModel.InitializationViewModel
import com.example.hotmovies.presentation.login.initialization.viewModel.InitializationViewModel.Actions.CheckSessionValidity
import com.example.hotmovies.presentation.login.login.LoginScreen
import com.example.hotmovies.presentation.login.navigation.LoginGraphRoutes.Login
import com.example.hotmovies.presentation.login.navigation.LoginGraphRoutes.SessionValidity
import com.example.hotmovies.presentation.login.viewModel.actions.LoginViewModel
import com.example.hotmovies.presentation.movies.navigation.MoviesGraph
import com.example.hotmovies.presentation.shared.LocalNavAnimatedVisibilityScope
import com.example.hotmovies.presentation.shared.LocalSharedTransitionScope
import com.example.hotmovies.presentation.shared.views.CustomDialogState
import com.example.hotmovies.presentation.shared.views.showDialog
import com.example.hotmovies.shared.Constants
import com.example.hotmovies.shared.getMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable

@Serializable
data object LoginGraph

@Serializable
private sealed interface LoginGraphRoutes {
    @Serializable
    data class Login(val redirectParams: String? = null) : LoginGraphRoutes

    @Serializable
    data object SessionValidity : LoginGraphRoutes
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.loginGraph(
    redirectUri: String,
    navController: NavController
) {
    navigation<LoginGraph>(startDestination = SessionValidity) {
        composable<SessionValidity> {

            val viewModel: InitializationViewModel = viewModel {
                val diContainer = CustomApplication.diContainer
                InitializationViewModel(
                    diContainer.loginRepository,
                    diContainer.secureRepository,
                    Dispatchers.IO
                )
            }

            val activity = LocalActivity.current
            val errorDialogState = showDialog(onCancel = { _ ->
                activity?.finish()
            }) {
                viewModel.doAction(CheckSessionValidity)
            }

            InitializationScreen(
                viewModel.state.collectAsStateWithLifecycle(),
                onSessionValid = { isValid ->
                    if (isValid) {
                        navController.navigate(MoviesGraph) {
                            popUpTo(LoginGraph) {
                                inclusive = true
                            }
                        }
                    } else navController.navigate(Login()) {
                        val appGraphId = navController.graph.startDestinationId
                        popUpTo(appGraphId) {
                            inclusive = true
                        }
                    }

                }, onError = {
                    errorDialogState.value = CustomDialogState(0, it.getMessage())
                })
        }
        composable<Login>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${redirectUri}/login?{redirectParams}"
                }
            ),
            enterTransition = {
                fadeIn(
                    initialAlpha = 0.0f,
                    animationSpec = tween(Constants.AnimationDurations.DEFAULT)
                )
            },
            exitTransition = {
                fadeOut(
                    targetAlpha = 0.0f,
                    animationSpec = tween(Constants.AnimationDurations.DEFAULT)
                )
            }
        ) {
            val viewModel: LoginViewModel = viewModel {
                val diContainer = CustomApplication.diContainer
                LoginViewModel(
                    createSavedStateHandle(),
                    diContainer.loginRepository,
                    diContainer.secureRepository
                )
            }

            with(LocalSharedTransitionScope.current) {
                CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this@composable) {
                    LoginScreen(
                        Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars.union(WindowInsets.displayCutout)),
                        viewModel.userNameText.collectAsStateWithLifecycle(),
                        viewModel.passwordText.collectAsStateWithLifecycle(),
                        viewModel.state,
                        viewModel::doAction
                    ) {
                        navController.navigate(MoviesGraph) {
                            popUpTo(LoginGraph) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }
}