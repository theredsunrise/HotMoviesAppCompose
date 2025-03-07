package com.example.hotmovies.appplication.login.interfaces

import com.example.hotmovies.domain.LoginPassword
import com.example.hotmovies.domain.LoginUserName
import kotlinx.coroutines.flow.Flow

interface LoginRepositoryInterface {
    sealed class Exceptions(msg: String) : Exception(msg) {
        class NoNetworkConnectionException : Exceptions("No network connection is available.")
        class InvalidCredentialsException : Exceptions("Invalid user name or password.")
    }

    fun login(userName: LoginUserName, password: LoginPassword): Flow<String>

    fun logout(): Flow<Unit>

    fun isSessionExpired(token: String): Flow<Boolean>
}