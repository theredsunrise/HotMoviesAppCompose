package com.example.hotmovies.appplication.login.interfaces

import com.example.hotmovies.domain.LoginPassword
import com.example.hotmovies.domain.LoginUserName
import kotlinx.coroutines.flow.Flow

interface LoginRepositoryInterface {
    sealed class Exceptions(msg: String) : Exception(msg) {
        class NoNetworkConnectionException : Exceptions("No network connection is available.")
    }

    fun login(userName: LoginUserName, password: LoginPassword): Flow<String>

    fun logout(sessionId: String): Flow<Boolean>

    fun isSessionValid(sessionId: String): Flow<Boolean>
}