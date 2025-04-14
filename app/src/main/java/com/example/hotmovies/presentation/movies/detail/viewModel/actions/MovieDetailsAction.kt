package com.example.hotmovies.presentation.movies.detail.viewModel.actions

import com.example.hotmovies.appplication.movies.MovieDetailsUseCase
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.presentation.movies.dtos.MovieDetailsUIMapper
import com.example.hotmovies.presentation.movies.dtos.MovieDetailsUIState
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateViewModelAction
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.none
import com.example.hotmovies.shared.progress
import com.example.hotmovies.shared.state
import com.example.hotmovies.shared.stateFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieDetailsAction(
    coroutineScope: CoroutineScope,
    private val movieDataRepository: MovieDataRepositoryInterface,
    imageIdToUrlMapper: MovieImageIdToUrlMapperInterface,
) :
    BaseResultStateViewModelAction<Int, MovieDetailsUIState>(coroutineScope, none) {

    private val uiMapper = MovieDetailsUIMapper(imageIdToUrlMapper)

    override fun action(value: Int): Flow<ResultState<MovieDetailsUIState>> {
        return MovieDetailsUseCase(movieDataRepository)(value).map {
            when (it) {
                is ResultState.None -> none
                is ResultState.Progress -> progress
                is ResultState.Failure -> it.exception.stateFailure()
                is ResultState.Success -> uiMapper.fromDomain(it.value).state()
            }
        }
    }
}

