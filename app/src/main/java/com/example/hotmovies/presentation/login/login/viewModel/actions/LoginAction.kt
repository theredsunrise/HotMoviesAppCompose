package com.example.hotmovies.presentation.login.viewModel.actions

import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.appplication.login.LoginUserUseCase
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateViewModelAction
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.none
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class LoginAction(
    coroutineScope: CoroutineScope,
    diContainer: DIContainer,
) :
    BaseResultStateViewModelAction<LoginUserUseCase.Credentials, Unit>(coroutineScope, none) {

    private val settingsRepository = diContainer.settingsRepository
    private val loginRepository = diContainer.loginRepository

    override fun action(value: LoginUserUseCase.Credentials): Flow<ResultState<Unit>> {
        return LoginUserUseCase(loginRepository, settingsRepository)(value)
    }

    fun login(userName: String, password: String) {
        run(LoginUserUseCase.Credentials(userName, password))
    }
}

