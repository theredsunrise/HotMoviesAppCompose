package com.example.hotmovies.infrastructure.dataRepository.tmdb

import com.example.hotmovies.BuildConfig
import com.example.hotmovies.infrastructure.dataRepository.tmdb.dtos.MovieDetailsDto
import com.example.hotmovies.infrastructure.dataRepository.tmdb.dtos.MoviesInfoDto
import com.example.hotmovies.infrastructure.dataRepository.tmdb.dtos.UserDto
import com.example.hotmovies.shared.toUse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbMovieDataApiInterface {
    @GET("3/account/{accountId}")
    suspend fun getUser(@Path("accountId") accountId: String): retrofit2.Response<UserDto>

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): retrofit2.Response<MovieDetailsDto>

    @GET("3/trending/movie/day")
    suspend fun getTrendingMoviesInfo(@Query("page") pageId: Int): retrofit2.Response<MoviesInfoDto>
}

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY.toUse(12)).build()
        val newRequest = request.newBuilder().url(newUrl).addHeader(
            "Authorization",
            "Bearer ${BuildConfig.TMDB_BEARER.toUse(45)}"
        ).build()
        return chain.proceed(newRequest)
    }
}

object TmdbMovieDataApiServiceFactory {
    private const val BASE_URL =
        "https://api.themoviedb.org/"
    private val instance: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(AuthorizationInterceptor())
            .cache(null)
            .build()
        instance = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
    }

    fun create(): TmdbMovieDataApiInterface {
        return instance.create(TmdbMovieDataApiInterface::class.java)
    }
}