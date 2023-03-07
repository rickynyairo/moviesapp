package com.ricky.moviesapp.persistence

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private lateinit var moviesDatabase: MoviesDatabase

    fun init(context: Context) {
        moviesDatabase = Room.databaseBuilder(context, MoviesDatabase::class.java, "movies")
            .build()
    }

    fun getMoviesDao(): MoviesDAO {
        return moviesDatabase.moviesDao()
    }
}