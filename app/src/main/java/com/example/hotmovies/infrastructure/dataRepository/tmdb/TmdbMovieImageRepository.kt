package com.example.hotmovies.infrastructure.dataRepository.tmdb

import android.graphics.Bitmap
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface.Exceptions.HttpException
import com.example.hotmovies.infrastructure.dataRepository.HttpMapper
import com.example.hotmovies.shared.checkNotMainThread
import com.example.hotmovies.shared.toBitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.InputStream

class TmdbMovieImageRepository(private val imageApiService: TmdbMovieImageApiInterface) :
    MovieImageRepositoryInterface {

    override fun getImage(id: String): InputStream {
        checkNotMainThread()
        val response = imageApiService.getImage(id).execute().apply {
            httpCodeError()?.also { msg ->
                throw HttpException(msg)
            }
        }
        return requireNotNull(response.body()).byteStream()
    }

    override fun getImageAsync(id: String): Flow<Bitmap> = flow {
        checkNotMainThread()
        val response = imageApiService.getImageAsync(id).apply {
            httpCodeError()?.also { msg ->
                throw HttpException(msg)
            }
        }
        emit(requireNotNull(response.body()).toBitmap())
    }

    private fun <T> Response<T>.httpCodeError(): String? {
        return if (!isSuccessful) HttpMapper.getMessage(code()) else null
    }
}