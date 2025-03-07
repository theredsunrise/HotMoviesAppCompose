package com.example.hotmovies.presentation.shared.pagers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hotmovies.appplication.movies.interfaces.MovieDataRepositoryInterface
import com.example.hotmovies.domain.Movie
import com.example.hotmovies.shared.checkNotMainThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlin.math.max

class MoviePagingSource(
    private val dataRepository: MovieDataRepositoryInterface,
    private val dispatcher: CoroutineDispatcher
) :
    PagingSource<Int, Movie>() {

    private val defaultPageId = 1
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val movie = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(movie.pageId)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val pageId = ensureValidKey(params.key)
        return dataRepository.getTrendingMoviesInfo(pageId, params.loadSize).map {
            checkNotMainThread()

            val result: LoadResult<Int, Movie> = LoadResult.Page(
                it.results, prevKey = if (pageId == defaultPageId) null else pageId - 1,
                nextKey = if (it.totalPages <= pageId || it.results.isEmpty()) null else pageId + 1
            )
            result
        }.catch { e ->
            if (e !is Exception) throw e
            emit(LoadResult.Error(e))
        }.flowOn(dispatcher).last()
    }

    private fun ensureValidKey(key: Int?) = max(key ?: defaultPageId, 1)
}