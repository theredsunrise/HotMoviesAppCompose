package com.example.hotmovies.presentation.login.navigation

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
import kotlinx.serialization.Serializable

@Serializable
data object LoginGraph

@Serializable
private sealed interface LoginGraphRoutes {
    @Serializable
    data object Login : LoginGraphRoutes

    @Serializable
    data object SessionValidity : LoginGraphRoutes
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.loginGraph(
    navController: NavController
) {
    navigation<LoginGraph>(startDestination = SessionValidity) {
        composable<SessionValidity> {

            val viewModel: InitializationViewModel = viewModel {
                InitializationViewModel(CustomApplication.diContainer)
            }

            val errorDialogState = showDialog(onCancel = { _ ->
                val appGraphId = navController.graph.startDestinationId
                navController.navigate(appGraphId) {
                    popUpTo(appGraphId) {
                        inclusive = true
                    }
                }
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
                    } else navController.navigate(Login) {
                        val appGraphId = navController.graph.startDestinationId
                        popUpTo(appGraphId) {
                            inclusive = true
                        }
                    }

                }, onError = {
                    errorDialogState.value = CustomDialogState(0, it.message.orEmpty())
                })
        }
        composable<Login>(
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
                LoginViewModel(createSavedStateHandle(), CustomApplication.diContainer)
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