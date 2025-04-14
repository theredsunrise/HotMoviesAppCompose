package com.example.hotmovies.presentation.movies.list.viewModel.actions

import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.appplication.login.LogoutUserCase
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SettingsRepositoryInterface
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateViewModelAction
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.none
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class LogoutAction(
    coroutineScope: CoroutineScope,
    private val loginRepository: LoginRepositoryInterface,
    private val settingsRepository: SettingsRepositoryInterface,
) :
    BaseResultStateViewModelAction<Unit, Unit>(coroutineScope, none) {

    override fun action(value: Unit): Flow<ResultState<Unit>> {
        return LogoutUserCase(loginRepository, settingsRepository)()
    }
}

