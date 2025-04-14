package com.example.hotmovies.presentation.login.initialization.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotmovies.appplication.login.interfaces.LoginRepositoryInterface
import com.example.hotmovies.appplication.login.interfaces.SettingsRepositoryInterface
import com.example.hotmovies.presentation.login.initialization.viewModel.InitializationViewModel.Actions.CheckSessionValidity
import com.example.hotmovies.presentation.login.initialization.viewModel.actions.SessionValidityAction
import kotlinx.coroutines.CoroutineDispatcher

class InitializationViewModel(
    loginRepository: LoginRepositoryInterface,
    settingsRepository: SettingsRepositoryInterface,
    dispatcher: CoroutineDispatcher
) :
    ViewModel() {
    private val sessionValidityAction =
        SessionValidityAction(viewModelScope, loginRepository, settingsRepository, dispatcher) {
            doAction(CheckSessionValidity)
        }

    val state = sessionValidityAction.state

    sealed interface Actions {
        data object CheckSessionValidity : Actions
    }

    fun doAction(action: Actions) {
        when (action) {
            CheckSessionValidity -> sessionValidityAction.run(Unit)
        }
    }
}