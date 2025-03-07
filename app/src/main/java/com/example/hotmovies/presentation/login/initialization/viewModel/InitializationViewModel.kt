package com.example.hotmovies.presentation.login.initialization.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotmovies.appplication.DIContainer
import com.example.hotmovies.presentation.initialization.viewModel.actions.SessionValidityAction
import com.example.hotmovies.presentation.login.initialization.viewModel.InitializationViewModel.Actions.CheckSessionValidity

class InitializationViewModel(diContainer: DIContainer) :
    ViewModel() {
    private val sessionValidityAction = SessionValidityAction(viewModelScope, diContainer) {
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