package com.example.hotmovies.domain

import com.example.hotmovies.domain.Movie.Genre
import com.example.hotmovies.domain.base.AbstractEntity

class MovieDetails private constructor(
    id: Int,
    val backdropPath: String?,
    val posterPath: String?,
    val title: String,
    val originalTitle: String,
    val overview: String?,
    val genres: List<Genre>,
    val revenue: Long,
    val voteAverage: Double,
    val voteCount: Long,
    val releaseDate: String
) : AbstractEntity(id) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MovieDetails

        if (backdropPath != other.backdropPath) return false
        if (posterPath != other.posterPath) return false
        if (title != other.title) return false
        if (originalTitle != other.originalTitle) return false
        if (overview != other.overview) return false
        if (genres != other.genres) return false
        if (revenue != other.revenue) return false
        if (voteAverage != other.voteAverage) return false
        if (voteCount != other.voteCount) return false
        if (releaseDate != other.releaseDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (backdropPath?.hashCode() ?: 0)
        result = 31 * result + (posterPath?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + originalTitle.hashCode()
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + genres.hashCode()
        result = 31 * result + revenue.toInt()
        result = 31 * result + voteAverage.hashCode()
        result = 31 * result + voteCount.toInt()
        result = 31 * result + releaseDate.hashCode()
        return result
    }

    companion object {
        operator fun invoke(
            id: Int,
            backdropPath: String?,
            posterPath: String?,
            title: String,
            originalTitle: String,
            overview: String?,
            genres: List<Genre>,
            revenue: Long,
            voteAverage: Double,
            voteCount: Long,
            releaseDate: String
        ): MovieDetails {
            return MovieDetails(
                id,
                backdropPath,
                posterPath,
                title,
                originalTitle,
                overview,
                genres,
                revenue,
                voteAverage,
                voteCount,
                releaseDate
            )
        }
    }
}