package com.example.hotmovies.infrastructure.mappers.tmdb

import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface
import javax.inject.Inject

class TmdbMovieIdToUrlMapper @Inject constructor() : MovieImageIdToUrlMapperInterface {

    private val BASE_URL =
        "https://image.tmdb.org/t/p/w500"

    init {
        println("******* ${this::class.simpleName}")
    }

    override fun toUrl(key: String?): String? {
        return key?.let { BASE_URL + it }
    }
}