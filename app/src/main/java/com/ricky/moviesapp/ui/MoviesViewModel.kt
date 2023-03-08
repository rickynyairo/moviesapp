package com.ricky.moviesapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricky.moviesapp.entity.Movie
import com.ricky.moviesapp.persistence.MoviesRepository
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    fun searchMovies(query: String) {
        viewModelScope.launch {
            val result = repository.searchMovies(query)
            _movies.postValue(result)
        }
    }

    fun getMovieFromApi(imdbId: String){
        viewModelScope.launch {
            val result = repository.findMovieFromApiByImdbId(imdbId)
            _movie.value = result
        }
    }
}