package com.example.hotmovies.appplication.movies.interfaces

import com.example.hotmovies.domain.MovieDetails
import com.example.hotmovies.domain.MoviesInfo
import com.example.hotmovies.domain.User
import kotlinx.coroutines.flow.Flow

interface MovieDataRepositoryInterface {
    sealed class Exceptions(msg: String) : Exception(msg) {
        class ResponseException(msg: String) : Exceptions(msg)
        class NoNetworkConnectionException : Exceptions("No network connection is available.")
    }

    fun getUser(): Flow<User>
    fun getMovieDetails(movieId: Int): Flow<MovieDetails>
    fun getTrendingMoviesInfo(pageId: Int, itemsPerPage: Int): Flow<MoviesInfo>
}