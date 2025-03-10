package com.example.hotmovies.infrastructure.mappers.tmdb

import com.example.hotmovies.appplication.movies.interfaces.MovieImageIdToUrlMapperInterface

class TmdbMovieIdToUrlMapper : MovieImageIdToUrlMapperInterface {

    private val BASE_URL =
        "https://image.tmdb.org/t/p/w500"

    override fun toUrl(key: String?): String? {
        return key?.let { BASE_URL + it }
    }
}