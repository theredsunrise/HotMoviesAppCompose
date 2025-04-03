package com.example.hotmovies.infrastructure.dataRepository.mock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface.Exceptions.NoNetworkConnectionException
import com.example.hotmovies.infrastructure.NetworkStatusResolver
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.security.InvalidParameterException

class MockMovieImageRepository(
    private val appContext: Context,
    @DrawableRes private val avatarId: Int
) :
    MovieImageRepositoryInterface {

    override fun getImage(id: String): InputStream {
        checkNotMainThread()
        Thread.sleep(1000)
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        if (id.isEmpty()) throw InvalidParameterException()

        val bmp = BitmapFactory.decodeResource(appContext.resources, avatarId)
        return ByteArrayOutputStream().use {
            try {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, it)
                ByteArrayInputStream(it.toByteArray())
            } finally {
                bmp.recycle()
            }
        }
    }

    override fun getImageAsync(id: String): Flow<Bitmap> = flow {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        emit(BitmapFactory.decodeResource(appContext.resources, avatarId))
    }
}