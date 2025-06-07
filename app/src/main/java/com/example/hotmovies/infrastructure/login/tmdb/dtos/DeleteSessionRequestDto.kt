package com.example.hotmovies.infrastructure.login.tmdb.dtos

import com.google.gson.annotations.SerializedName

data class DeleteSessionRequestDto(
    @SerializedName("session_id") val sessionId: String
)
