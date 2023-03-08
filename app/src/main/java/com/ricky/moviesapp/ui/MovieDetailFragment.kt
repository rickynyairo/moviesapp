package com.ricky.moviesapp.ui

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ricky.moviesapp.MainActivity
import com.ricky.moviesapp.R
import com.ricky.moviesapp.api.OmdbApiClient
import com.ricky.moviesapp.databinding.MovieDetailFragmentBinding
import com.ricky.moviesapp.entity.Movie
import com.ricky.moviesapp.persistence.DatabaseProvider
import com.ricky.moviesapp.persistence.MoviesRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieDetailFragment : Fragment() {

    private var _binding: MovieDetailFragmentBinding? = null
    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var moviesRepository: MoviesRepository
    private lateinit var args: MovieDetailFragmentArgs
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        args = MovieDetailFragmentArgs.fromBundle(requireArguments())
        moviesRepository = MoviesRepository(OmdbApiClient(), DatabaseProvider.getMoviesDao())
        moviesViewModel = MoviesViewModel(moviesRepository)
        _binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.Default).launch {
            moviesViewModel.getMovieFromApi(args.imdbID)
        }
        var theMovie: Movie? = null
        moviesViewModel.movie.observe(viewLifecycleOwner) { movie ->
            binding.apply {
                titleTextView.text = movie.title
                yearTextView.text = movie.year
                typeTextView.text = movie.type
                ratingTextView.text = movie.rating
                genreTextView.text = movie.genre
                plotTextView.text = movie.plot
                actorsTextView.text = movie.actors
            }
            Picasso.get().load(movie.posterUrl).placeholder(R.drawable.placeholder)
                .into(binding.posterImageView)
            theMovie = movie
        }
        var main = activity as MainActivity
        main.fab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                CoroutineScope(Dispatchers.Default).launch {
                    moviesRepository.saveMovie(theMovie!!)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}