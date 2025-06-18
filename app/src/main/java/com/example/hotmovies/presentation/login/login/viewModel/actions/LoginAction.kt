package com.example.hotmovies.presentation.login.viewModel.actions

import com.example.hotmovies.appplication.login.LoginUserUseCase
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateViewModelAction
import com.example.hotmovies.shared.ResultState
import kotlinx.coroutines.flow.Flow

class LoginAction(
    private val loginRepository: LoginRepositoryInterface,
    private val secureRepository: SecureRepositoryInterface,
) :
    BaseResultStateViewModelAction<LoginUserUseCase.Credentials, Unit>() {

    init {
        println("******* ${this::class.simpleName}")
    }

    override fun action(value: LoginUserUseCase.Credentials): Flow<ResultState<Unit>> {
        return LoginUserUseCase(loginRepository, secureRepository)(value)
    }

    fun login(userName: String, password: String) {
        run(LoginUserUseCase.Credentials(userName, password))
    }
}

