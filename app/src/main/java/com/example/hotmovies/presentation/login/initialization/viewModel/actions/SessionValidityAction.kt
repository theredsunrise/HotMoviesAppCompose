package com.example.hotmovies.presentation.initialization.viewModel.actions

import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.appplication.login.SessionValidityUseCase
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateEventViewModelAction
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.eventNone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionValidityAction(
    coroutineScope: CoroutineScope,
    diContainer: DIContainer,
    onStart: (suspend () -> Unit)
) :
    BaseResultStateEventViewModelAction<Unit, Boolean>(
        coroutineScope,
        eventNone,
        1,
        onStart = onStart
    ) {

    private val loginRepository = diContainer.loginRepository
    private val settingsRepository = diContainer.settingsRepository

    override fun action(value: Unit): Flow<Event<ResultState<Boolean>>> {
        return SessionValidityUseCase(loginRepository, settingsRepository)()
            .map { Event(it) }
    }
}

