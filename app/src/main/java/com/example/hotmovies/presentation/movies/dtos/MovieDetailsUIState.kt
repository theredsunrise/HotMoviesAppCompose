package com.example.hotmovies.presentation.movies.dtos

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.MovieDetails
import com.example.hotmovies.shared.Event

@Stable
@Immutable
class MovieDetailsUIState private constructor(
    val id: Int,
    val backdropUrl: String?,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val voteAverage: Float,
    val isLoaded: Event<Boolean>
) {

    companion object {
        operator fun invoke(
            id: Int,
            backdropUrl: String?,
            title: String,
            overview: String,
            posterUrl: String?,
            voteAverage: Float,
            isLoaded: Event<Boolean>

        ): MovieDetailsUIState {
            return MovieDetailsUIState(
                id, backdropUrl, title, overview, posterUrl, voteAverage, isLoaded
            )
        }

        fun defaultState() = MovieDetailsUIState(0, null, "", "", null, 0f, Event(false))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieDetailsUIState

        if (id != other.id) return false
        if (voteAverage != other.voteAverage) return false
        if (backdropUrl != other.backdropUrl) return false
        if (title != other.title) return false
        if (overview != other.overview) return false
        if (posterUrl != other.posterUrl) return false
        if (isLoaded != other.isLoaded) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + voteAverage.hashCode()
        result = 31 * result + (backdropUrl?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + (posterUrl?.hashCode() ?: 0)
        result = 31 * result + isLoaded.hashCode()
        return result
    }
}

class MovieDetailsUIMapper(private val idToUrlMapper: MovieImageIdToUrlMapperInterface) {
    fun fromDomain(movieDetails: MovieDetails): MovieDetailsUIState {
        return MovieDetailsUIState.invoke(
            movieDetails.id,
            idToUrlMapper.toUrl(movieDetails.backdropPath),
            movieDetails.title,
            movieDetails.overview.orEmpty(),
            idToUrlMapper.toUrl(movieDetails.posterPath),
            movieDetails.voteAverage.toFloat() * 5f * 0.1f,
            Event(false)
        )
    }
}