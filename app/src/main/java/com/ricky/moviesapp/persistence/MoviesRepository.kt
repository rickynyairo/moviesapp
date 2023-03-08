package com.ricky.moviesapp.persistence


import com.ricky.moviesapp.api.OmdbApiClient
import com.ricky.moviesapp.entity.Movie

class MoviesRepository(private val omdbApi: OmdbApiClient, private val moviesDAO: MoviesDAO) {

    suspend fun searchMovies(query: String): List<Movie> {
        return omdbApi.searchMovies(query)
    }

    suspend fun findMovieFromApiByImdbId(imdbId: String): Movie {
        return omdbApi.findByImdbId(imdbId)

    }

    fun saveMovie(movie: Movie) {
        moviesDAO.save(movie)
    }

    fun getMovies(): List<Movie>{
        return moviesDAO.getAll()
    }

    fun deleteMovie(movie: Movie) {
        moviesDAO.delete(movie)
    }

    fun getMovie(imdbId: String): Movie? {
        return moviesDAO.findById(imdbId)
    }

}