package com.example.hotmovies.domain

import android.os.Parcelable
import com.example.hotmovies.domain.base.AbstractEntity
import kotlinx.parcelize.Parcelize

@Parcelize
class Movie private constructor(
    val idParcelize: Int,
    val pageId: Int,
    val backdropPath: String?,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String?,
    val genreIds: List<Genre>,
    val popularity: Double,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Long
) : AbstractEntity(idParcelize), Parcelable {

    enum class Genre {
        ACTION,
        ADVENTURE,
        ANIMATION,
        COMEDY,
        CRIME,
        DOCUMENTARY,
        DRAMA,
        FAMILY,
        FANTASY,
        HISTORY,
        HORROR,
        MUSIC,
        MYSTERY,
        ROMANCE,
        SCIENCE_FICTION,
        TV_MOVIE,
        THRILLER,
        WAR,
        WESTERN
    }

    companion object {
        operator fun invoke(
            id: Int,
            pageId: Int,
            backdropPath: String?,
            title: String,
            originalTitle: String,
            overview: String,
            posterPath: String?,
            genreIds: List<Genre>,
            popularity: Double,
            releaseDate: String,
            voteAverage: Double,
            voteCount: Long
        ): Movie {
            return Movie(
                id,
                pageId,
                backdropPath,
                title,
                originalTitle,
                overview,
                posterPath,
                genreIds,
                popularity,
                releaseDate,
                voteAverage,
                voteCount
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Movie

        if (pageId != other.pageId) return false
        if (backdropPath != other.backdropPath) return false
        if (title != other.title) return false
        if (originalTitle != other.originalTitle) return false
        if (overview != other.overview) return false
        if (posterPath != other.posterPath) return false
        if (genreIds != other.genreIds) return false
        if (popularity != other.popularity) return false
        if (releaseDate != other.releaseDate) return false
        if (voteAverage != other.voteAverage) return false
        if (voteCount != other.voteCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + pageId
        result = 31 * result + (backdropPath?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + originalTitle.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + (posterPath?.hashCode() ?: 0)
        result = 31 * result + genreIds.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + voteAverage.hashCode()
        result = 31 * result + voteCount.toInt()
        return result
    }
}
