package com.example.hotmovies.di

import android.content.Context
import com.example.hotmovies.R
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.infrastructure.login.LoginRepository
import com.example.hotmovies.infrastructure.login.SessionProviderInterface
import com.example.hotmovies.infrastructure.login.tmdb.TmdbSessionProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginSingletonModule {

    companion object {
        @Singleton
        @Provides
        @TmdbModuleRedirectUri
        fun providesRedirectUri(@ApplicationContext appContext: Context): String {
            return appContext.getString(R.string.redirect_uri)
        }
    }

    @Singleton
    @Binds
    abstract fun bindsSessionProvider(impl: TmdbSessionProvider): SessionProviderInterface


    @Singleton
    @Binds
    abstract fun bindsLoginRepository(impl: LoginRepository): LoginRepositoryInterface
}

