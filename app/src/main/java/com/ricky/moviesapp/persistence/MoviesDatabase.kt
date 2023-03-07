package com.ricky.moviesapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ricky.moviesapp.entity.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDAO
}