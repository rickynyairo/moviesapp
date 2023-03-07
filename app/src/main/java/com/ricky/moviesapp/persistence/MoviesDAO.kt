package com.ricky.moviesapp.persistence

import androidx.room.*
import com.ricky.moviesapp.entity.Movie

/**
 * The Database Access Object.
 * You will work on defining the database interactions in this class.
 */
@Dao
interface MoviesDAO {
    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>

    // OnConflictStrategy.REPLACE allows the the database to override an entry that already exists.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg movies: Movie)

    @Query("SELECT * FROM movies WHERE imdbID = :imdbID")
    fun findById(imdbID: String): Movie?

    @Delete
    fun delete(movie: Movie)
}