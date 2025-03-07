package com.example.hotmovies.presentation.movies.list.uiStateProcessors

import androidx.compose.runtime.Composable
import com.example.hotmovies.presentation.movies.list.MoviesScreenErrorSource
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.LoadMovies
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.LoadUserDetails
import com.example.hotmovies.presentation.shared.helpers.LifecycleAwareFlowsCollector
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.checkMainThread
import kotlinx.coroutines.flow.Flow


@Composable
fun ProcessUserDetails(
    userDetailsFlow: Flow<Event<ResultState<Boolean>>>,
    onAction: (MoviesViewModel.Actions) -> Unit,
    onError: (source: MoviesScreenErrorSource, exception: Exception) -> Unit
) {

    LifecycleAwareFlowsCollector(userDetailsFlow) { userDetailsActionEvent ->
        checkMainThread()

        if (userDetailsActionEvent.content is ResultState.Failure) {
            onError(
                MoviesScreenErrorSource.LoadUserDetailsError,
                userDetailsActionEvent.content.exception
            )
            return@LifecycleAwareFlowsCollector
        }

        val userDetailsAction =
            userDetailsActionEvent.getContentIfNotHandled() ?: return@LifecycleAwareFlowsCollector
        when {
            userDetailsAction.isSuccessTrue -> onAction(LoadMovies)
            userDetailsAction.isSuccessFalse -> onAction(LoadUserDetails)
        }
    }
}