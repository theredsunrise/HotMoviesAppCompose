package com.example.hotmovies.presentation.login.initialization.viewModel.actions

import com.example.hotmovies.appplication.login.SessionValidityUseCase
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateEventViewModelAction
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionValidityAction(
    private val loginRepository: LoginRepositoryInterface,
    private val secureRepository: SecureRepositoryInterface,
    private val dispatcher: CoroutineDispatcher,
    onStart: (suspend () -> Unit)
) :
    BaseResultStateEventViewModelAction<Unit, Boolean>(
        1,
        onStart = onStart
    ) {

    override fun action(value: Unit): Flow<Event<ResultState<Boolean>>> {
        return SessionValidityUseCase(loginRepository, secureRepository, dispatcher)()
            .map { Event(it) }
    }
}

