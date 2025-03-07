package com.example.hotmovies.appplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.hotmovies.R
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SettingsRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.Movie
import com.example.hotmovies.infrastructure.LoginRepository
import com.example.hotmovies.infrastructure.SettingsRepository
import com.example.hotmovies.infrastructure.dataRepository.mock.MockMovieDataRepository
import com.example.hotmovies.infrastructure.dataRepository.mock.MockMovieImageRepository
import com.example.hotmovies.infrastructure.dataRepository.tmdb.TmdbMovieDataApiInterface
import com.example.hotmovies.infrastructure.dataRepository.tmdb.TmdbMovieDataApiServiceFactory
import com.example.hotmovies.infrastructure.dataRepository.tmdb.TmdbMovieDataRepository
import com.example.hotmovies.infrastructure.dataRepository.tmdb.TmdbMovieImageApiFactory
import com.example.hotmovies.infrastructure.dataRepository.tmdb.TmdbMovieImageRepository
import com.example.hotmovies.infrastructure.mappers.tmdb.TmdbMovieIdToUrlMapper
import com.example.hotmovies.presentation.shared.pagers.MoviePagingSource
import kotlinx.coroutines.Dispatchers

class DIContainer(val appContext: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val tmdbMovieDataApiService: TmdbMovieDataApiInterface by lazy {
        TmdbMovieDataApiServiceFactory.create()
    }

    private val tmdbMovieImageApiService: com.example.hotmovies.infrastructure.dataRepository.tmdb.TmdbMovieImageApiInterface by lazy {
        TmdbMovieImageApiFactory.create()
    }

    val loginRepository: LoginRepositoryInterface by lazy {
        LoginRepository(appContext)
    }

    val settingsRepository: SettingsRepositoryInterface by lazy {
        SettingsRepository(appContext.dataStore)
    }

    val tmdbPager: Pager<Int, Movie> by lazy {
        val pagingConfig = PagingConfig(20, enablePlaceholders = false)
        Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                MoviePagingSource(
                    tmdbMovieDataRepository,
                    Dispatchers.IO
                )
            }
        )
    }

    val tmdbMovieImageIdToUrlMapper: MovieImageIdToUrlMapperInterface by lazy {
        TmdbMovieIdToUrlMapper()
    }

    val tmdbMovieImageRepository: MovieImageRepositoryInterface by lazy {
        TmdbMovieImageRepository(tmdbMovieImageApiService)
    }

    val _tmdbMovieImageRepository: MovieImageRepositoryInterface by lazy {
        MockMovieImageRepository(appContext, R.drawable.vector_background)
    }

    val tmdbMovieDataRepository: MovieDataRepositoryInterface by lazy {
        TmdbMovieDataRepository(tmdbMovieDataApiService, tmdbMovieImageRepository)
    }

    val _tmdbMovieDataRepository: MovieDataRepositoryInterface by lazy {
        MockMovieDataRepository(appContext, R.drawable.vector_background, true)
    }

    val previewMovieDataRepository: MovieDataRepositoryInterface by lazy {
        MockMovieDataRepository(appContext, R.drawable.spongebob, false)
    }
}