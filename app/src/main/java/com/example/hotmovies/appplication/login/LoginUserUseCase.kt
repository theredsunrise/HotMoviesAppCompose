package com.example.hotmovies.appplication.login

import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface.Keys.AUTH_TOKEN_KEY
import com.example.hotmovies.domain.LoginPassword
import com.example.hotmovies.domain.LoginUserName
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.asStateResult
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalCoroutinesApi::class)

class LoginUserUseCase(
    private val loginRepository: LoginRepositoryInterface,
    private val secureRepository: SecureRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    data class Credentials(val userName: String, val password: String)
    data class CredentialsInternal(val userName: LoginUserName, val password: LoginPassword)

    operator fun invoke(credentials: Credentials): Flow<ResultState<Unit>> = flow {
        checkNotMainThread()
        val userName = LoginUserName.invoke(credentials.userName)
        val password = LoginPassword.invoke(credentials.password)
        emit(CredentialsInternal(userName, password))

    }.flatMapLatest { entity ->
        checkNotMainThread()
        loginRepository.login(entity.userName, entity.password)

    }.flatMapLatest { token ->
        checkNotMainThread()
        secureRepository.store(
            AUTH_TOKEN_KEY,
            token
        )

    }.asStateResult().flowOn(dispatcher)
}