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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MoviesInfo

        if (page != other.page) return false
        if (totalPages != other.totalPages) return false
        if (totalResults != other.totalResults) return false
        if (results != other.results) return false

        return true
    }

    override fun hashCode(): Int {
        var result = page
        result = 31 * result + totalPages
        result = 31 * result + totalResults
        result = 31 * result + results.hashCode()
        return result
    }
}