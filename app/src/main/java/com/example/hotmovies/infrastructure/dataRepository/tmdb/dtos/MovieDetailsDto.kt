package com.example.hotmovies.infrastructure.dataRepository.tmdb.dtos

import com.example.hotmovies.domain.MovieDetails
import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<DetailsGenreDto>,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("origin_country") val originCountry: List<String>,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyDto>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: Long,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Long
) {

    data class DetailsGenreDto(
        @SerializedName("id") val id: Int,
    )

    fun toDomain() = MovieDetails(
        id,
        backdropPath,
        posterPath,
        title,
        originalTitle,
        overview,
        genres.map { detailGenre ->
            val genre = GenreDto.values().first { genre -> genre.id == detailGenre.id }
            GenreDto.toDomain(genre)
        },
        revenue,
        voteAverage,
        voteCount,
        releaseDate
    )
}

data class ProductionCompanyDto(
    @SerializedName("id") val id: Int,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("name") val name: String,
    @SerializedName("origin_country") val originCountry: String
)