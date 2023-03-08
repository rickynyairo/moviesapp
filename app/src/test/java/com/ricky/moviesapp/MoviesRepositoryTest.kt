package com.ricky.moviesapp

import com.ricky.moviesapp.api.OmdbApiClient
import com.ricky.moviesapp.entity.Movie
import com.ricky.moviesapp.persistence.MoviesDAO
import com.ricky.moviesapp.persistence.MoviesRepository
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    private lateinit var movieRepository: MoviesRepository

    @Mock
    private lateinit var movieDao: MoviesDAO

    @MockK(relaxed = true)
    private lateinit var movieApiClient: OmdbApiClient

    @Before
    fun setup() {
        movieApiClient = mockk(relaxed = true)
        movieRepository = MoviesRepository(movieApiClient, movieDao)
    }

    @After
    fun tearDown(){
        movieRepository.getMovies().map {
            movieDao.delete(it)
        }
        clearAllMocks()
    }

    @Test
    fun `get movies from database`() = runBlocking {
        val movieList = listOf(
            Movie("tt1234567", "Movie 1", "2021", "movie", "Poster 1", ),
            Movie("tt7654321", "Movie 2", "2022", "series", "Poster 2"),
            Movie("tt0987654", "Movie 3", "2023", "movie", "Poster 3")
        )
        `when`(movieDao.getAll()).thenReturn(movieList)

        val result = movieRepository.getMovies()

        assertEquals(movieList, result)
    }


    @Test
    fun `save movies to database`() = runBlocking {
        val movie = Movie("tt1234567", "Movie 1", "2021", "movie", "Poster 1", )
        movieRepository.saveMovie(movie)

        verify(movieDao, times(1)).save(movie)
    }
}
