package com.arctouch.codechallenge.model

data class GenreResponse(val genres: List<Genre>)

data class Genre(val id: Int, val name: String)

data class UpcomingMoviesResponse(
    val page: Int,
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String?,
    val genres: List<Genre>?,
    val genreIds: List<Int>?,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?
)
