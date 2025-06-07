package com.example.hotmovies.infrastructure.login

import kotlinx.coroutines.flow.Flow

interface SessionProviderInterface {
    sealed class Exceptions(msg: String) : Exception(msg) {
        class ResponseException(msg: String) : Exceptions(msg)
        class NoNetworkConnectionException : Exceptions("No network connection is available.")
        class FailedToRetrieveSessionException : Exceptions("Failed to retrieve a session.")
        class FailedToDeleteSessionException : Exceptions("Failed to delete a session.")

    }

    fun getRequestToken(): Flow<String>
    fun validateRequestTokenByCredentials(
        userName: String,
        password: String,
        requestToken: String
    ): Flow<String>

    fun getSessionFromRedirectParams(redirectParams: String): Flow<String>
    fun getSessionFromRequestToken(requestToken: String): Flow<String>
    fun deleteSession(sessionId: String): Flow<Boolean>
}