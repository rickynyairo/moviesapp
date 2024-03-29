package com.ricky.moviesapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricky.moviesapp.entity.Movie
import com.ricky.moviesapp.persistence.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val _movies: MutableLiveData<List<Movie>> = MutableLiveData()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _movie: MutableLiveData<Movie> = MutableLiveData()
    val movie: LiveData<Movie>
        get() = _movie
    fun searchMovies(query: String) {
        viewModelScope.launch {
            val result = repository.searchMovies(query)
            val savedMovies = repository.getMovies()
            val hiddenMovieHash = hashMapOf<String, Boolean>()
            savedMovies.map { movie ->
                if (movie.hidden) {
                    hiddenMovieHash[movie.imdbID] = true
                }
            }
            val resultsWithoutHiddenMovies = result.filterNot { searchedMovie ->
                hiddenMovieHash.containsKey(searchedMovie.imdbID)
            }
            _movies.postValue(resultsWithoutHiddenMovies)
        }
    }

    suspend fun getMovie(imdbId: String){
        var movie = repository.getMovie(imdbId)
        if (movie != null){
            // movie exists locally
            _movie.postValue(movie!!)
        }else{
            movie = repository.findMovieFromApiByImdbId(imdbId)
            _movie.postValue(movie!!)
        }
    }

    fun getSavedMovies(){
        val result = repository.getMovies()
        _movies.postValue(result)
    }
}