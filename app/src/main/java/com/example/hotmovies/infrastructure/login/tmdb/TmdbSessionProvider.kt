package com.example.hotmovies.infrastructure.login.tmdb

import android.content.Context
import androidx.core.net.toUri
import com.example.hotmovies.infrastructure.NetworkStatusResolver
import com.example.hotmovies.infrastructure.login.SessionProviderInterface
import com.example.hotmovies.infrastructure.login.SessionProviderInterface.Exceptions.FailedToDeleteSessionException
import com.example.hotmovies.infrastructure.login.SessionProviderInterface.Exceptions.FailedToRetrieveSessionException
import com.example.hotmovies.infrastructure.login.SessionProviderInterface.Exceptions.NoNetworkConnectionException
import com.example.hotmovies.infrastructure.login.SessionProviderInterface.Exceptions.ResponseException
import com.example.hotmovies.infrastructure.login.tmdb.dtos.DeleteSessionRequestDto
import com.example.hotmovies.shared.checkNotMainThread
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class TmdbSessionProvider(
    private val appContext: Context,
    private val redirectUri: String,
    private val api: TmdbAuthenticationApiInterface
) : SessionProviderInterface {

    override fun validateRequestTokenByCredentials(
        userName: String,
        password: String,
        requestToken: String
    ): Flow<String> = flow {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

        val response = api.validateRequestTokenByCredentials(
            userName,
            password,
            requestToken
        ).apply {
            errorInBody()?.also { msg ->
                throw ResponseException(msg)
            }
        }
        if (!response.isSuccessful) {
            throw FailedToRetrieveSessionException()
        }
        val dto = requireNotNull(response.body())
        if (!dto.success) {
            throw FailedToRetrieveSessionException()
        }
        emit(dto.requestToken)
    }

    override fun getRequestToken(): Flow<String> = flow {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

        val response = api.getRequestToken().apply {
            errorInBody()?.also { msg ->
                throw ResponseException(msg)
            }
        }
        if (!response.isSuccessful) {
            throw FailedToRetrieveSessionException()
        }
        val dto = requireNotNull(response.body())
        if (!dto.success) {
            throw FailedToRetrieveSessionException()
        }
        emit(dto.requestToken)
    }

    override fun getSessionFromRedirectParams(redirectParams: String): Flow<String> {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

        val fullUri = "${redirectUri}?${redirectParams}".toUri()
        val requestToken =
            fullUri.getQueryParameter("request_token") ?: throw FailedToRetrieveSessionException()
        fullUri.getQueryParameter("approved") ?: throw FailedToRetrieveSessionException()
        return getSessionFromRequestToken(requestToken)
    }

    override fun getSessionFromRequestToken(requestToken: String) = flow {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

        val response = api.getSession(requestToken).apply {
            errorInBody()?.also { msg ->
                throw ResponseException(msg)
            }
        }
        if (!response.isSuccessful) {
            throw FailedToRetrieveSessionException()
        }
        val dto = requireNotNull(response.body())
        if (!dto.success) {
            throw FailedToRetrieveSessionException()
        }
        emit(dto.sessionId)
    }

    override fun deleteSession(sessionId: String) = flow {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

        val response = api.deleteSession(
            DeleteSessionRequestDto(sessionId)
        ).apply {
            errorInBody()?.also { msg ->
                throw ResponseException(msg)
            }
        }
        if (!response.isSuccessful) {
            throw FailedToDeleteSessionException()
        }
        val dto = requireNotNull(response.body())
        emit(dto.success)
    }

    private fun <T> Response<T>.errorInBody(): String? {
        return errorBody()?.string()?.let {
            Gson().fromJson(it, JsonObject::class.java).get("status_message").asString
        }
    }
}