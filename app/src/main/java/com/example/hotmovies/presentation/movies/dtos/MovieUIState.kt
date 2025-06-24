package com.example.hotmovies.presentation.movies.dtos

import androidx.compose.runtime.Immutable
import androidx.paging.LoadState.Loading
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.Movie
import com.example.hotmovies.shared.Event

@Immutable
data class MovieUIState(
    val id: Int,
    val pageId: Int,
    val backdropUrl: String?,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val voteAverage: Float,
    val isLoaded: Event<Boolean>
) {

    val finalBackDropImageUrl: String = backdropUrl ?: posterUrl.orEmpty()
    val backDropTransitionKey: String = "BackDrop_${id}_${pageId}"

    companion object {
        operator fun invoke(
            id: Int,
            pageId: Int,
            backdropUrl: String?,
            title: String,
            overview: String,
            posterUrl: String?,
            voteAverage: Float,
            isLoaded: Event<Boolean>

        ): MovieUIState {
            return MovieUIState(
                id, pageId, backdropUrl, title, overview, posterUrl, voteAverage, isLoaded
            )
        }
    }
}

class MovieUIMapper(private val idToUrlMapper: MovieImageIdToUrlMapperInterface) {
    fun fromDomain(movie: Movie): MovieUIState {
        return MovieUIState.invoke(
            movie.id,
            movie.pageId,
            idToUrlMapper.toUrl(movie.backdropPath),
            movie.title,
            movie.overview,
            idToUrlMapper.toUrl(movie.posterPath),
            movie.voteAverage.toFloat() * 5f * 0.1f,
            Event(false)
        )
    }
}

val pagingDataProgress = PagingData.from(
    emptyList<MovieUIState>(), sourceLoadStates = LoadStates(
        Loading,
        Loading,
        Loading
    )
)