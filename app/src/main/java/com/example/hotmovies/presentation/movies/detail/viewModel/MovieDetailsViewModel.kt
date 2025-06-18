package com.example.hotmovies.presentation.movies.detail.viewModel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.presentation.movies.detail.viewModel.MovieDetailsViewModel.Actions.LoadMovieDetails
import com.example.hotmovies.presentation.movies.detail.viewModel.actions.MovieDetailsAction
import com.example.hotmovies.presentation.movies.dtos.MovieDetailsUIState
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.checkMainThread
import com.example.hotmovies.shared.progress
import com.example.hotmovies.shared.progressEvent
import com.example.hotmovies.shared.stateEvent
import com.example.hotmovies.shared.stateEventFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    movieDataRepository: MovieDataRepositoryInterface,
    imageIdToUrlMapper: MovieImageIdToUrlMapperInterface
) : ViewModel() {

    @Stable
    @Immutable
    data class UIState(
        val movieDetails: MovieDetailsUIState,
        val loadAction: Event<ResultState<Boolean>>
    ) {
        companion object {
            fun defaultState() = UIState(MovieDetailsUIState.defaultState(), false.stateEvent())
        }
    }

    private val movieDetailsAction =
        MovieDetailsAction(movieDataRepository, imageIdToUrlMapper)

    private var _state = MutableStateFlow(UIState.defaultState())
    val state = _state.asStateFlow()

    init {
        movieDetailsAction.state.onEach { result ->
            checkMainThread()
            when (result) {
                is ResultState.None -> {}
                is ResultState.Success -> {
                    _state.update {
                        it.copy(
                            result.value,
                            loadAction = true.stateEvent()
                        )
                    }
                }

                is progress -> _state.update { it.copy(loadAction = progressEvent) }
                is ResultState.Failure -> _state.update { it.copy(loadAction = result.exception.stateEventFailure()) }
            }
        }.launchIn(viewModelScope)
    }

    sealed interface Actions {
        data class LoadMovieDetails(val movieId: Int) : Actions
    }

    fun doAction(action: Actions) {
        when (action) {
            is LoadMovieDetails -> movieDetailsAction.run(action.movieId)
        }
    }
}