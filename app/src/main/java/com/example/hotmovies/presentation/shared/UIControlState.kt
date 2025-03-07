package com.example.hotmovies.presentation.shared

data class UIControlState(val isEnabled: Boolean, val exception: Exception?) {
    companion object {
        fun enabled() = UIControlState(true, null)
        fun disabled() = UIControlState(false, null)
    }
}
