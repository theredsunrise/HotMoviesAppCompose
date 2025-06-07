package com.example.hotmovies.infrastructure.login.tmdb.dtos

import com.google.gson.annotations.SerializedName

data class DeleteSessionDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: Int?,
    @SerializedName("status_message") val statusMessage: String?
)