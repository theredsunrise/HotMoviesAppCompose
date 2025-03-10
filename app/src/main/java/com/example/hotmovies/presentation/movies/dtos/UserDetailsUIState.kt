package com.example.hotmovies.presentation.movies.dtos

import android.content.res.Resources
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.hotmovies.R
import com.example.hotmovies.domain.User

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