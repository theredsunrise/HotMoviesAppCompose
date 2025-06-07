package com.example.hotmovies.appplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.hotmovies.R
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SecureRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.Movie
import com.example.hotmovies.infrastructure.data.mock.MockMovieDataRepository
import com.example.hotmovies.infrastructure.data.mock.MockMovieImageRepository
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieDataApiInterface
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieDataApiServiceFactory
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieDataRepository
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieImageApiFactory
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieImageApiInterface
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieImageRepository
import com.example.hotmovies.infrastructure.login.LoginRepository
import com.example.hotmovies.infrastructure.login.SessionProviderInterface
import com.example.hotmovies.infrastructure.login.tmdb.TmdbAuthenticationApiFactory
import com.example.hotmovies.infrastructure.login.tmdb.TmdbAuthenticationApiInterface
import com.example.hotmovies.infrastructure.login.tmdb.TmdbSessionProvider
import com.example.hotmovies.infrastructure.mappers.tmdb.TmdbMovieIdToUrlMapper
import com.example.hotmovies.infrastructure.storage.Crypto
import com.example.hotmovies.infrastructure.storage.CryptoInterface
import com.example.hotmovies.infrastructure.storage.SecureRepository
import com.example.hotmovies.presentation.shared.pagers.MoviePagingSource
import kotlinx.coroutines.Dispatchers

class DIContainer(val appContext: Context) {

    private val tmdbMovieDataApiService: TmdbMovieDataApiInterface by lazy {
        TmdbMovieDataApiServiceFactory.create()
    }

    private val tmdbMovieImageApiService: TmdbMovieImageApiInterface by lazy {
        TmdbMovieImageApiFactory.create()
    }

    private val authenticationApiService: TmdbAuthenticationApiInterface by lazy {
        TmdbAuthenticationApiFactory.create()
    }

    private val sessionProvider: SessionProviderInterface by lazy {
        TmdbSessionProvider(
            appContext,
            appContext.getString(R.string.redirect_uri),
            authenticationApiService
        )
    }

    val loginRepository: LoginRepositoryInterface by lazy {
        LoginRepository(appContext, sessionProvider)
    }

    private val crypto: CryptoInterface by lazy {
        Crypto()
    }

    val secureRepository: SecureRepositoryInterface by lazy {
        SecureRepository(crypto, dataStore)
    }

    private val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile(appContext.getString(R.string.secure_datastore_file)) }
        )
    }
    val pager: Pager<Int, Movie> by lazy {
        val pagingConfig = PagingConfig(20, enablePlaceholders = false)
        Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                MoviePagingSource(
                    movieDataRepository,
                    Dispatchers.IO
                )
            }
        )
    }

    val movieImageIdToUrlMapper: MovieImageIdToUrlMapperInterface by lazy {
        TmdbMovieIdToUrlMapper()
    }

    val movieImageRepository: MovieImageRepositoryInterface by lazy {
        TmdbMovieImageRepository(tmdbMovieImageApiService)
    }

    val _movieImageRepository: MovieImageRepositoryInterface by lazy {
        MockMovieImageRepository(appContext, R.drawable.vector_background)
    }

    val movieDataRepository: MovieDataRepositoryInterface by lazy {
        TmdbMovieDataRepository(
            appContext,
            tmdbMovieDataApiService,
            movieImageRepository,
            secureRepository
        )
    }

    val _movieDataRepository: MovieDataRepositoryInterface by lazy {
        MockMovieDataRepository(appContext, R.drawable.vector_background, true)
    }

    val previewMovieDataRepository: MovieDataRepositoryInterface by lazy {
        MockMovieDataRepository(appContext, R.drawable.vector_background, false)
    }
}