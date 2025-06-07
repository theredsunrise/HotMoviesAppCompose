package com.example.hotmovies.presentation.movies.list.viewModel.actions

import com.example.hotmovies.appplication.login.LogoutUserCase
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateViewModelAction
import com.example.hotmovies.shared.ResultState
import kotlinx.coroutines.flow.Flow

class LogoutAction(
    private val loginRepository: LoginRepositoryInterface,
    private val secureRepository: SecureRepositoryInterface,
) :
    BaseResultStateViewModelAction<Unit, Unit>() {

    override fun action(value: Unit): Flow<ResultState<Unit>> {
        return LogoutUserCase(loginRepository, secureRepository)()
    }
}

