package com.example.hotmovies.infrastructure

import android.content.Context
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface.Exceptions.InvalidCredentialsException
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface.Exceptions.NoNetworkConnectionException
import com.example.hotmovies.domain.LoginPassword
import com.example.hotmovies.domain.LoginUserName
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import kotlin.random.Random

class LoginRepository(private val appContext: Context) : LoginRepositoryInterface {
    private val messageDigest = MessageDigest.getInstance("MD5")

    @OptIn(ExperimentalStdlibApi::class)
    override fun login(userName: LoginUserName, password: LoginPassword): Flow<String> = flow {
        checkNotMainThread()
        delay(1000)
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        val passwordDigest = messageDigest.digest(password.value.toByteArray()).toHexString()
        if (userName.value == "test123" && passwordDigest == "e807f1fcf82d132f9bb018ca6738a19f")
            emit(Random.nextInt().toString())
        else
            throw InvalidCredentialsException()
    }

    override fun logout(): Flow<Unit> = flow {
        checkNotMainThread()
        delay(1000)
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        emit(Unit)
    }

    override fun isSessionValid(token: String): Flow<Boolean> = flow {
        checkNotMainThread()
        delay(500)
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        emit(true)
    }

}
