package com.example.hotmovies.infrastructure.login.tmdb.dtos

import com.google.gson.annotations.SerializedName

data class SessionDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("session_id") val sessionId: String
)