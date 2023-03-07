package com.ricky.moviesapp

import OmdbApiClient

class MoviesRepository(private val omdbApi: OmdbApiClient) {

    suspend fun searchMovies(query: String): List<Movie> {
        val response = omdbApi.searchMovies(query)
        return response.map {
            Movie(it.title, it.year, it.imdbID, it.type, it.posterUrl)
        }
    }
}