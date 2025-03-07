package com.example.hotmovies.appplication.login

import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SettingsRepositoryInterface
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.asStateResult
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalCoroutinesApi::class)
class SessionValidityUseCase(
    private val loginRepository: LoginRepositoryInterface,
    private val settingsRepository: SettingsRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(): Flow<ResultState<Boolean>> =
        settingsRepository.getStringValue(SettingsRepositoryInterface.Keys.AUTH_TOKEN_KEY)
            .flatMapLatest { token ->
                checkNotMainThread()
                loginRepository.isSessionExpired(token)
            }
            .catch { e ->
                if (e !is SettingsRepositoryInterface.Exceptions.NoValueException) throw e
                emit(false)
            }
            .asStateResult().flowOn(dispatcher)
}