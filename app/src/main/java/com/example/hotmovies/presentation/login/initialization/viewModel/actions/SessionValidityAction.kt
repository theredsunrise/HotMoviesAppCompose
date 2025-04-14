package com.example.hotmovies.presentation.login.initialization.viewModel.actions

import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.appplication.login.SessionValidityUseCase
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SettingsRepositoryInterface
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateEventViewModelAction
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.eventNone
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionValidityAction(
    coroutineScope: CoroutineScope,
    private val loginRepository: LoginRepositoryInterface,
    private val settingsRepository: SettingsRepositoryInterface,
    private val dispatcher: CoroutineDispatcher,
    onStart: (suspend () -> Unit)
) :
    BaseResultStateEventViewModelAction<Unit, Boolean>(
        coroutineScope,
        eventNone,
        1,
        onStart = onStart
    ) {

    override fun action(value: Unit): Flow<Event<ResultState<Boolean>>> {
        return SessionValidityUseCase(loginRepository, settingsRepository, dispatcher)()
            .map { Event(it) }
    }
}

