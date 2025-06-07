package com.example.hotmovies.infrastructure.login.tmdb.dtos

import com.google.gson.annotations.SerializedName

data class RequestTokenDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("request_token") val requestToken: String,
)