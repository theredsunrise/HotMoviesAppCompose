package com.example.hotmovies.appplication.login

import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface.Keys.AUTH_TOKEN_KEY
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.asStateResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transformLatest

class LogoutUserCase(
    private val loginRepository: LoginRepositoryInterface,
    private val secureRepository: SecureRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<ResultState<Unit>> = flowOf(Unit).transformLatest {
        try {
            delay(1000) // just due to the prolonged operation
            val sessionId = secureRepository.getStringValue(AUTH_TOKEN_KEY).first()
            loginRepository.logout(sessionId).firstOrNull()
        } catch (e: Exception) {
        }
        secureRepository.clear(AUTH_TOKEN_KEY).firstOrNull()
        emit(Unit)
    }.asStateResult().flowOn(dispatcher)
}