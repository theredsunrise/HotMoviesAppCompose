package com.example.hotmovies.presentation.movies.dtos

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.LoadState.Loading
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.Movie

@Stable
@Immutable
class MovieUI private constructor(
    val id: Int,
    val pageId: Int,
    val backdropUrl: String?,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val voteAverage: Double,
) {

    companion object {
        operator fun invoke(
            id: Int,
            pageId: Int,
            backdropUrl: String?,
            title: String,
            overview: String,
            posterUrl: String?,
            voteAverage: Double,

            ): MovieUI {
            return MovieUI(
                id, pageId, backdropUrl, title, overview, posterUrl, voteAverage
            )
        }
    }
}

class MovieUIMapper(private val idToUrlMapper: MovieImageIdToUrlMapperInterface) {
    fun fromDomain(movie: Movie): MovieUI {
        return MovieUI.invoke(
            movie.id,
            movie.pageId,
            idToUrlMapper.toUrl(movie.backdropPath),
            movie.title,
            movie.overview,
            idToUrlMapper.toUrl(movie.posterPath),
            movie.voteAverage
        )
    }
}

val pagingDataProgress = PagingData.from(
    emptyList<MovieUI>(), sourceLoadStates = LoadStates(
        Loading,
        Loading,
        Loading
    )
)