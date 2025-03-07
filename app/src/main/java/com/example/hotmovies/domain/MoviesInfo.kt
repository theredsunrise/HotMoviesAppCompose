package com.example.hotmovies.domain

import com.example.hotmovies.domain.base.ValueObject

class MoviesInfo private constructor(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
) : ValueObject {
    companion object {
        operator fun invoke(
            page: Int,
            results: List<Movie>,
            totalPages: Int,
            totalResults: Int
        ): MoviesInfo {
            return MoviesInfo(page, results, totalPages, totalResults)
        }
    }
}