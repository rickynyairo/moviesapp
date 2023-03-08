package com.ricky.moviesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ricky.moviesapp.persistence.MoviesRepository

class MoviesViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
