package com.example.hotmovies.appplication.login

import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SettingsRepositoryInterface
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.asStateResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

class LogoutUserCase(
    private val loginRepository: LoginRepositoryInterface,
    private val settingsRepository: SettingsRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<ResultState<Unit>> = loginRepository.logout()
        .flatMapLatest {
            settingsRepository.clear(SettingsRepositoryInterface.Keys.AUTH_TOKEN_KEY)
        }.asStateResult().flowOn(dispatcher)
}