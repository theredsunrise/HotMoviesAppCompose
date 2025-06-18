package com.example.hotmovies.infrastructure.login

import android.content.Context
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface.Exceptions.NoNetworkConnectionException
import com.example.hotmovies.domain.LoginPassword
import com.example.hotmovies.domain.LoginUserName
import com.example.hotmovies.infrastructure.NetworkStatusResolver
import com.example.hotmovies.shared.checkNotMainThread
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val sessionProvider: SessionProviderInterface
) : LoginRepositoryInterface {

    init {
        println("******* ${this::class.simpleName}")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun login(userName: LoginUserName, password: LoginPassword) =
        sessionProvider
            .getRequestToken()
            .flatMapLatest { requestToken ->
                sessionProvider.validateRequestTokenByCredentials(
                    userName.value,
                    password.value,
                    requestToken
                )
            }.flatMapLatest { requestToken ->
                sessionProvider.getSessionFromRequestToken(requestToken)
            }

    override fun logout(sessionId: String): Flow<Boolean> = sessionProvider.deleteSession(sessionId)

    // Mocked, TMDB's session never expires.
    override fun isSessionValid(sessionId: String): Flow<Boolean> = flow {
        checkNotMainThread()
        delay(500)
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        emit(true)
    }
}
