package com.example.hotmovies.infrastructure.dataRepository.tmdb.dtos

import com.example.hotmovies.domain.Movie
import com.example.hotmovies.domain.Movie.Genre
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("genre_ids") val genreIds: List<GenreDto>,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Long
) {

    fun toDomain(pageId: Int) =
        Movie(
            id,
            pageId,
            backdropPath,
            title,
            originalTitle,
            overview,
            posterPath,
            genreIds.map { GenreDto.toDomain(it) },
            popularity,
            releaseDate,
            voteAverage,
            voteCount
        )
}

enum class GenreDto(val id: Int) {
    @SerializedName("28")
    ACTION(28),

    @SerializedName("12")
    ADVENTURE(12),

    @SerializedName("16")
    ANIMATION(16),

    @SerializedName("35")
    COMEDY(35),

    @SerializedName("80")
    CRIME(80),

    @SerializedName("99")
    DOCUMENTARY(99),

    @SerializedName("18")
    DRAMA(18),

    @SerializedName("10751")
    FAMILY(10751),

    @SerializedName("14")
    FANTASY(14),

    @SerializedName("36")
    HISTORY(36),

    @SerializedName("27")
    HORROR(27),

    @SerializedName("10402")
    MUSIC(10402),

    @SerializedName("9648")
    MYSTERY(9648),

    @SerializedName("10749")
    ROMANCE(10749),

    @SerializedName("878")
    SCIENCE_FICTION(878),

    @SerializedName("10770")
    TV_MOVIE(10770),

    @SerializedName("53")
    THRILLER(53),

    @SerializedName("10752")
    WAR(10752),

    @SerializedName("37")
    WESTERN(37);

    companion object {
        fun toDomain(genreDto: GenreDto): Genre =
            when (genreDto) {
                ACTION -> Genre.ACTION
                ADVENTURE -> Genre.ADVENTURE
                ANIMATION -> Genre.ANIMATION
                COMEDY -> Genre.COMEDY
                CRIME -> Genre.CRIME
                DOCUMENTARY -> Genre.DOCUMENTARY
                DRAMA -> Genre.DRAMA
                FAMILY -> Genre.FAMILY
                FANTASY -> Genre.FANTASY
                HISTORY -> Genre.HISTORY
                HORROR -> Genre.HORROR
                MUSIC -> Genre.MUSIC
                MYSTERY -> Genre.MYSTERY
                ROMANCE -> Genre.ROMANCE
                SCIENCE_FICTION -> Genre.SCIENCE_FICTION
                TV_MOVIE -> Genre.TV_MOVIE
                THRILLER -> Genre.THRILLER
                WAR -> Genre.WAR
                WESTERN -> Genre.WESTERN
            }
    }
}
