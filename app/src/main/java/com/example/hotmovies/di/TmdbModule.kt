package com.example.hotmovies.di

import android.content.Context
import android.content.res.Resources
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieDataApiInterface
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieDataApiServiceFactory
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieImageApiFactory
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieImageApiInterface
import com.example.hotmovies.infrastructure.login.tmdb.TmdbAuthenticationApiFactory
import com.example.hotmovies.infrastructure.login.tmdb.TmdbAuthenticationApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbModuleRedirectUri

@Module
@InstallIn(SingletonComponent::class)
object TmdbModule {
    @Singleton
    @Provides
    fun providesResources(@ApplicationContext appContext: Context): Resources {
        return appContext.resources
    }

    @Singleton
    @Provides
    fun providesTmdbMovieDataApiService(): TmdbMovieDataApiInterface {
        return TmdbMovieDataApiServiceFactory.create()
    }

    @Singleton
    @Provides
    fun providesTmdbMovieImageApiService(): TmdbMovieImageApiInterface {
        return TmdbMovieImageApiFactory.create()
    }

    @Singleton
    @Provides
    fun providesTmdbAuthenticationApiService(): TmdbAuthenticationApiInterface {
        return TmdbAuthenticationApiFactory.create()
    }
}
