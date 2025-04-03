package com.example.hotmovies.appplication.login.interfaces

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface MovieImageRepositoryInterface {
    sealed class Exceptions(msg: String) : Exception(msg) {
        class HttpException(msg: String) : Exceptions(msg)
        class NoNetworkConnectionException : Exceptions("No network connection is available.")
    }

    fun getImage(id: String): InputStream
    fun getImageAsync(id: String): Flow<Bitmap>
}