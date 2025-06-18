package com.example.hotmovies.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.hotmovies.appplication.login.interfaces.MovieImageRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.Movie
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieDataRepository
import com.example.hotmovies.infrastructure.data.tmdb.TmdbMovieImageRepository
import com.example.hotmovies.infrastructure.mappers.tmdb.TmdbMovieIdToUrlMapper
import com.example.hotmovies.presentation.shared.pagers.MoviePagingSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object MoviesModule {

    @ViewModelScoped
    @Provides
    fun providesPager(
        moviePagingSource: MoviePagingSource
    ): Pager<Int, Movie> {
        val pagingConfig = PagingConfig(20, enablePlaceholders = false)
        println("******* MoviePagingSource")
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                moviePagingSource
            }
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesSingletonModule {

    @Singleton
    @Binds
    abstract fun bindsMovieImageIdToUrlMapper(impl: TmdbMovieIdToUrlMapper): MovieImageIdToUrlMapperInterface

    @Singleton
    @Binds
    abstract fun bindsMovieDataRepositoryInterface(impl: TmdbMovieDataRepository): MovieDataRepositoryInterface

    @Singleton
    @Binds
    abstract fun bindsMovieImageRepository(impl: TmdbMovieImageRepository): MovieImageRepositoryInterface
}