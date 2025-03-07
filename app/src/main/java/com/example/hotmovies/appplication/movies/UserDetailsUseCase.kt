package com.example.hotmovies.appplication.movies.interfaces

import com.example.hotmovies.domain.User
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.asStateResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class UserDetailsUseCase(
    private val movieDataRepository: MovieDataRepositoryInterface,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(): Flow<ResultState<User>> =
        movieDataRepository.getUser().asStateResult().flowOn(dispatcher)
}