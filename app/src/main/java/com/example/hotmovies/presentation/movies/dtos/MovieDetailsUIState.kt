package com.example.hotmovies.presentation.movies.dtos

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.MovieDetails
import com.example.hotmovies.shared.Event

@Stable
@Immutable
data class MovieDetailsUIState private constructor(
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