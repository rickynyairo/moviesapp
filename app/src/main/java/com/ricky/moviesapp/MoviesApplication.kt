package com.ricky.moviesapp

import android.app.Application
import com.ricky.moviesapp.persistence.DatabaseProvider

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.init(this)
    }
}
