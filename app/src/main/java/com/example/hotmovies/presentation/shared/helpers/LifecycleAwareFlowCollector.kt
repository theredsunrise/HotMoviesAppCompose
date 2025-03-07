package com.example.hotmovies.presentation.shared.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> LifecycleAwareFlowsCollector(stateFlow: Flow<T>, onCollect: (T) -> Unit) {
    val flowState by stateFlow.collectAsStateWithLifecycle(null)
    LaunchedEffect(flowState) {
        flowState?.also {
            onCollect(it)
        }
    }
}
