package com.example.hotmovies.appplication.movies

import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.domain.MovieDetails
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.asStateResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class MovieDetailsUseCase(
    private val movieDataRepository: MovieDataRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(movieId: Int): Flow<ResultState<MovieDetails>> =
        movieDataRepository.getMovieDetails(movieId).asStateResult().flowOn(dispatcher)
}