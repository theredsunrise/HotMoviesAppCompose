package com.example.hotmovies.infrastructure.data.tmdb

import android.content.Context
import android.graphics.Bitmap
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface.Keys.AUTH_TOKEN_KEY
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface.Exceptions.NoNetworkConnectionException
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface.Exceptions.ResponseException
import com.example.hotmovies.domain.MovieDetails
import com.example.hotmovies.domain.MoviesInfo
import com.example.hotmovies.domain.User
import com.example.hotmovies.infrastructure.NetworkStatusResolver
import com.example.hotmovies.shared.checkNotMainThread
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transformLatest
import retrofit2.Response

class TmdbMovieDataRepository(
    private val appContext: Context,
    private val dataApiService: TmdbMovieDataApiInterface,
    private val imageRepository: MovieImageRepositoryInterface,
    private val secureRepository: SecureRepositoryInterface
) : MovieDataRepositoryInterface {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUser(): Flow<User> =
        secureRepository.getStringValue(AUTH_TOKEN_KEY).transformLatest { sessionId ->
            checkNotMainThread()
            if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

            val userResponse = dataApiService.getUser(sessionId).apply {
                errorInBody()?.also { msg ->
                    throw ResponseException(msg)
                }
            }
            val userDto = requireNotNull(userResponse.body())
            val avatarPath = userDto.avatar.tmdb?.avatarPath

            val avatar: Bitmap? = if (avatarPath != null) {
                imageRepository.getImageAsync(avatarPath).firstOrNull()
            } else null

            emit(userDto.toDomain(avatar))
        }

    override fun getMovieDetails(movieId: Int): Flow<MovieDetails> = flow {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

        val movieDetailsResponse = dataApiService.getMovieDetails(movieId).apply {
            errorInBody()?.also { msg ->
                throw ResponseException(msg)
            }
        }
        emit(requireNotNull(movieDetailsResponse.body()).toDomain())
    }

    override fun getTrendingMoviesInfo(pageId: Int, itemsPerPage: Int): Flow<MoviesInfo> = flow {
        checkNotMainThread()
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()

        val moviesResponse = dataApiService.getTrendingMoviesInfo(pageId).apply {
            errorInBody()?.also { msg ->
                throw ResponseException(msg)
            }
        }
        emit(requireNotNull(moviesResponse.body()).toDomain())
    }

    private fun <T> Response<T>.errorInBody(): String? {
        return errorBody()?.string()?.let {
            Gson().fromJson(it, JsonObject::class.java).get("status_message").asString
        }
    }
}