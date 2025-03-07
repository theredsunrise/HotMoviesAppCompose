package com.example.hotmovies.presentation.shared.viewModels

import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
abstract class BaseViewModelAction<I, O>(
    coroutineScope: CoroutineScope,
    initValue: O,
    replay: Int = 0,
    onStart: (suspend () -> Unit)? = null
) {
    private var trigger =
        MutableSharedFlow<I>(replay, 1, BufferOverflow.DROP_OLDEST)

    protected abstract fun action(value: I): Flow<O>

    val state: StateFlow<O> =
        trigger.onStart {
            onStart?.invoke()
        }.debounce(100).flatMapLatest { input ->
            action(input)
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(8000), initValue)

    fun run(input: I) {
        trigger.tryEmit(input)
    }
}

typealias BaseResultStateViewModelAction<I, O> = BaseViewModelAction<I, ResultState<O>>
typealias BaseResultStateEventViewModelAction<I, O> = BaseViewModelAction<I, Event<ResultState<O>>>