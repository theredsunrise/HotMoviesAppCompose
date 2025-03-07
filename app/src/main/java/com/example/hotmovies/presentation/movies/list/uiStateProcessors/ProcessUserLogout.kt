package com.example.hotmovies.presentation.movies.list.uiStateProcessors

import android.app.Activity
import androidx.compose.runtime.Composable
import com.example.hotmovies.presentation.movies.list.MoviesScreenErrorSource
import com.example.hotmovies.presentation.shared.helpers.LifecycleAwareFlowsCollector
import com.example.hotmovies.presentation.shared.isEnabled
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.checkMainThread
import kotlinx.coroutines.flow.Flow

@Composable
fun ProcessUserLogout(
    activity: Activity?,
    logoutFlow: Flow<Event<ResultState<Boolean>>>,
    onLogout: () -> Unit,
    onError: (source: MoviesScreenErrorSource, exception: Exception) -> Unit
) {
    LifecycleAwareFlowsCollector(logoutFlow) { logoutActionEvent ->
        checkMainThread()

        activity?.isEnabled = !logoutActionEvent.content.isProgress
        if (
            logoutActionEvent.content is ResultState.Failure) {
            onError(
                MoviesScreenErrorSource.LogoutError,
                logoutActionEvent.content.exception
            )
            return@LifecycleAwareFlowsCollector
        }

        val userDetailsAction =
            logoutActionEvent.getContentIfNotHandled() ?: return@LifecycleAwareFlowsCollector
        when {
            userDetailsAction.isSuccessTrue -> onLogout()
        }
    }
}