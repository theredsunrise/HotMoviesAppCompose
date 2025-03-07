package com.example.hotmovies.appplication.movies.interfaces

interface MovieImageIdToUrlMapperInterface {
    fun toUrl(key: String?): String?
}