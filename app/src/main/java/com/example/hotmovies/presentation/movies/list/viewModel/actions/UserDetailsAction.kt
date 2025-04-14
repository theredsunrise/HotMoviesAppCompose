package com.example.hotmovies.presentation.movies.list.viewModel.actions

import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.appplication.movies.interfaces.UserDetailsUseCase
import com.example.hotmovies.domain.User
import com.example.hotmovies.presentation.shared.viewModels.BaseResultStateViewModelAction
import com.example.hotmovies.shared.ResultState
import com.example.hotmovies.shared.none
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class UserDetailsAction(
    coroutineScope: CoroutineScope,
    private val movieDataRepository: MovieDataRepositoryInterface
) : BaseResultStateViewModelAction<Unit, User>(coroutineScope, none) {

    override fun action(value: Unit): Flow<ResultState<User>> {
        return UserDetailsUseCase(movieDataRepository)()
    }
}