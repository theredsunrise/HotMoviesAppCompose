package com.example.hotmovies.presentation.shared.helpers

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hotmovies.shared.checkMainThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun <T> LifecycleAwareFlowsCollector(stateFlow: Flow<T>, onCollect: (T) -> Unit) {
    stateFlow.onEach { flowState ->
        checkMainThread()
        flowState?.also { onCollect(it) }
    }.collectAsStateWithLifecycle(null)
}
