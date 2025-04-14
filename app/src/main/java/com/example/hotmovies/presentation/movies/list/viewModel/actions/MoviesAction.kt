package com.example.hotmovies.presentation.movies.list.viewModel.actions

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import com.example.hotmovies.domain.Movie
import com.example.hotmovies.presentation.movies.dtos.MovieUIMapper
import com.example.hotmovies.presentation.movies.dtos.MovieUIState
import com.example.hotmovies.presentation.movies.dtos.pagingDataProgress
import com.example.hotmovies.presentation.shared.viewModels.BaseViewModelAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesAction(
    coroutineScope: CoroutineScope,
    private val moviePager: Pager<Int, Movie>,
    imageIdToUrlMapper: MovieImageIdToUrlMapperInterface,
    private val cacheCoroutineScope: CoroutineScope
) : BaseViewModelAction<Unit, PagingData<MovieUIState>>(coroutineScope, pagingDataProgress) {

    private val uiMapper = MovieUIMapper(imageIdToUrlMapper)

    override fun action(value: Unit): Flow<PagingData<MovieUIState>> {
        return moviePager.flow.map { pagingData -> pagingData.map { uiMapper.fromDomain(it) } }
            .cachedIn(cacheCoroutineScope)
    }
}