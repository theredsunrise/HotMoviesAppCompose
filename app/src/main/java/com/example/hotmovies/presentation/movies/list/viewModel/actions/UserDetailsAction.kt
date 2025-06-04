package com.example.hotmovies.presentation.movies.list.viewModel.actions

import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.UserDetailsUseCase
import com.example.hotmovies.domain.User
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateViewModelAction
import com.example.hotmovies.shared.ResultState
import kotlinx.coroutines.flow.Flow

class UserDetailsAction(
    private val movieDataRepository: MovieDataRepositoryInterface
) : BaseResultStateViewModelAction<Unit, User>() {

    override fun action(value: Unit): Flow<ResultState<User>> {
        return UserDetailsUseCase(movieDataRepository)()
    }
}