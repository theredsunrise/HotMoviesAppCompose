package com.example.hotmovies.presentation.movies.list.viewModel

import android.content.res.Resources
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.hotmovies.R
import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.domain.User
import com.example.hotmovies.presentation.movies.dtos.MovieUI
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.LoadMovies
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.LoadUserDetails
import com.example.hotmovies.presentation.movies.list.viewModel.MoviesViewModel.Actions.Logout
import com.example.hotmovies.presentation.movies.list.viewModel.actions.LogoutAction
import com.example.hotmovies.presentation.movies.list.viewModel.actions.MoviesAction
import com.example.hotmovies.presentation.movies.list.viewModel.actions.UserDetailsAction
import com.example.hotmovies.shared.Event
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.checkMainThread
import com.example.hotmovies.shared.none
import com.example.hotmovies.shared.progress
import com.example.hotmovies.shared.progressEvent
import com.example.hotmovies.shared.stateEvent
import com.example.hotmovies.shared.stateEventFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MoviesViewModel(diContainer: DIContainer): ViewModel() {

    @Stable
    @Immutable
    data class UserDetailsUIState(
        val name: String,
        val userName: String,
        val overview: String,
        val avatar: ImageBitmap?,
    ) {
        companion object {
            fun defaultState() = UserDetailsUIState(
                "",
                "",
                "",
                null
            )

            fun fromDomain(resources: Resources, user: User): UserDetailsUIState {
                return UserDetailsUIState(
                    user.name,
                    user.userName,
                    resources.getString(R.string.my_overview),
                    user.avatar?.let { it.asImageBitmap() })
            }
        }
    }

    @Stable
    @Immutable
    data class UIState(
        val userDetails: UserDetailsUIState,
        var movieDetailAction: Event<Boolean>,
        val userDetailsAction: Event<ResultState<Boolean>>,
        val logoutAction: Event<ResultState<Boolean>>
    ) {
        companion object {
            fun defaultState() =
                UIState(
                    UserDetailsUIState.defaultState(),
                    Event(false),
                    false.stateEvent(),
                    false.stateEvent()
                )
        }
    }

    private val resources = diContainer.appContext.resources
    private val logoutAction = LogoutAction(viewModelScope, diContainer)
    private val moviesAction = MoviesAction(viewModelScope, diContainer, viewModelScope)
    private val userDetailsAction = UserDetailsAction(viewModelScope, diContainer)

    private var _state = MutableStateFlow(UIState.defaultState())
    val state = _state.asStateFlow()
    val moviesPagingData: StateFlow<PagingData<MovieUI>> = moviesAction.state

    init {
        userDetailsAction.state.onEach { result ->
            checkMainThread()
            when (result) {
                is none -> {}
                is ResultState.Success -> {
                    _state.update {
                        it.copy(
                            userDetails = UserDetailsUIState.fromDomain(
                                resources,
                                result.value
                            ),
                            userDetailsAction = true.stateEvent()
                        )
                    }

                }

                is progress -> _state.update { it.copy(userDetailsAction = progressEvent) }
                is ResultState.Failure -> _state.update { it.copy(userDetailsAction = result.exception.stateEventFailure()) }
            }
        }.launchIn(viewModelScope)

        logoutAction.state.onEach { result ->
            checkMainThread()
            when (result) {
                is none -> {}
                is ResultState.Success -> {
                    _state.update {
                        it.copy(
                            logoutAction = true.stateEvent()
                        )
                    }
                }

                is progress -> _state.update {
                    it.copy(
                        userDetailsAction = progressEvent,
                        logoutAction = progressEvent
                    )
                }

                is ResultState.Failure -> _state.update { it.copy(logoutAction = result.exception.stateEventFailure()) }
            }
        }.launchIn(viewModelScope)
    }

    sealed interface Actions {
        data class ShowingMovieDetail(val isActive: Boolean) : Actions
        data object LoadUserDetails : Actions
        data object LoadMovies : Actions
        data object Logout : Actions
    }

    fun doAction(action: Actions) {
        when (action) {
            is Actions.ShowingMovieDetail -> {
                _state.update {
                    it.copy(
                        movieDetailAction = Event(
                            action.isActive
                        )
                    )
                }
            }

            is LoadMovies -> moviesAction.run(Unit)
            is LoadUserDetails -> userDetailsAction.run(Unit)
            is Logout -> logoutAction.run(Unit)
        }
    }
}