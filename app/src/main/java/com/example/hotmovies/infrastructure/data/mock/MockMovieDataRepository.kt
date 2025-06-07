package com.example.hotmovies.infrastructure.data.mock

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface.Exceptions.NoNetworkConnectionException
import com.example.hotmovies.domain.Movie
import com.example.hotmovies.domain.MovieDetails
import com.example.hotmovies.domain.MoviesInfo
import com.example.hotmovies.domain.User
import com.example.hotmovies.infrastructure.NetworkStatusResolver
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockMovieDataRepository(
    private val appContext: Context,
    @DrawableRes private val avatarId: Int,
    private val isCheckNetworkConnection: Boolean
) :
    MovieDataRepositoryInterface {

    override fun getUser(): Flow<User> = flow {
        checkNotMainThread()
        delay(1000)
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        val user = User(
            123,
            "Brano Bench",
            "theredsunrise",
            BitmapFactory.decodeResource(appContext.resources, avatarId)
        )
        emit(user)
    }

    override fun getMovieDetails(movieId: Int): Flow<MovieDetails> = flow {
        checkNotMainThread()
        delay(1000)
        if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        val movieDetails = MovieDetails(
            1,
            "backdrop",
            "poster",
            "Title 0987654321",
            "Original title",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
            listOf(Movie.Genre.TV_MOVIE),
            565877,
            7.66,
            876786,
            "14 May 2014"
        )
        emit(movieDetails)
    }

    override fun getTrendingMoviesInfo(pageId: Int, itemsPerPage: Int): Flow<MoviesInfo> = flow {
        if (isCheckNetworkConnection) {
            if (!NetworkStatusResolver.isNetworkAvailable(appContext)) throw NoNetworkConnectionException()
        }
        val movies = (1..40).map { id ->
            Movie(
                id,
                1,
                "backdrop",
                "Test title 1234567890",
                "Original title",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                "poster",
                listOf(Movie.Genre.TV_MOVIE),
                4.0,
                "October",
                3.0,
                345
            )
        }
        val moviesInfo = MoviesInfo(pageId, movies, 2, 40)
        emit(moviesInfo)
    }
}