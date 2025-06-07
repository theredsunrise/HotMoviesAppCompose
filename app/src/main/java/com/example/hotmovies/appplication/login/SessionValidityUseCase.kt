package com.example.hotmovies.appplication.login

import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface.Exceptions.NoValueException
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface.Keys.AUTH_TOKEN_KEY
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.asStateResult
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalCoroutinesApi::class)
class SessionValidityUseCase(
    private val loginRepository: LoginRepositoryInterface,
    private val secureRepository: SecureRepositoryInterface,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(): Flow<ResultState<Boolean>> =
        secureRepository.getStringValue(AUTH_TOKEN_KEY)
            .flatMapLatest { token ->
                checkNotMainThread()
                loginRepository.isSessionValid(token)
            }
            .catch { e ->
                if (e !is NoValueException) throw e
                emit(false)
            }
            .asStateResult().flowOn(dispatcher)
}