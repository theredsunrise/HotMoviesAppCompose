package com.example.hotmovies.infrastructure.dataRepository.tmdb.dtos

import android.graphics.Bitmap
import com.example.hotmovies.domain.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("avatar") val avatar: AvatarDto
) {
    fun toDomain(avatar: Bitmap?) = User(id, name, username, avatar)
}

data class AvatarDto(
    @SerializedName("gravatar") val gravatar: GravatarDto?,
    @SerializedName("tmdb") val tmdb: TmdbDto?
)

data class GravatarDto(
    @SerializedName("hash") val hash: String
)

data class TmdbDto(
    @SerializedName("avatar_path") val avatarPath: String
)
