package com.example.hotmovies.infrastructure.dataRepository.tmdb.dtos

import com.example.hotmovies.domain.MoviesInfo
import com.google.gson.annotations.SerializedName

data class MoviesInfoDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
) {
    fun toDomain() = MoviesInfo(page, results.map { it.toDomain(page) }, totalPages, totalResults)
}