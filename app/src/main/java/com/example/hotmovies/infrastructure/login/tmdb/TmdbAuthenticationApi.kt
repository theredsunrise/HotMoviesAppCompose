package com.example.hotmovies.infrastructure.login.tmdb

import com.example.hotmovies.BuildConfig
import com.example.hotmovies.infrastructure.login.tmdb.dtos.DeleteSessionDto
import com.example.hotmovies.infrastructure.login.tmdb.dtos.DeleteSessionRequestDto
import com.example.hotmovies.infrastructure.login.tmdb.dtos.RequestTokenDto
import com.example.hotmovies.infrastructure.login.tmdb.dtos.SessionDto
import com.example.hotmovies.shared.toUse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface TmdbAuthenticationApiInterface {
    @GET("token/new")
    suspend fun getRequestToken(): Response<RequestTokenDto>

    @FormUrlEncoded
    @POST("token/validate_with_login")
    suspend fun validateRequestTokenByCredentials(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("request_token") requestToken: String
    ): Response<RequestTokenDto>

    @FormUrlEncoded
    @POST("session/new")
    suspend fun getSession(
        @Field("request_token") requestToken: String
    ): Response<SessionDto>

    @HTTP(method = "DELETE", path = "session", hasBody = true)
    suspend fun deleteSession(
        @Body sessionBody: DeleteSessionRequestDto
    ): Response<DeleteSessionDto>

}

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY.toUse(12)).build()
        val newRequest = request.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}

object TmdbAuthenticationApiFactory {
    private const val BASE_URL =
        "https://api.themoviedb.org/3/authentication/"
    private val instance: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(AuthorizationInterceptor())
            .cache(null)
            .build()
        instance =
            Retrofit.Builder().baseUrl(
                BASE_URL
            )
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
    }

    fun create(): TmdbAuthenticationApiInterface {
        return instance.create(
            TmdbAuthenticationApiInterface::class.java
        )
    }
}