package com.ricky.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var parentActivity: MainActivity
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentActivity = requireActivity() as MainActivity
        args = MovieDetailFragmentArgs.fromBundle(requireArguments())
        moviesRepository = MoviesRepository(OmdbApiClient(), DatabaseProvider.getMoviesDao())
        moviesViewModel = MoviesViewModel(moviesRepository)
        _binding = MovieDetailFragmentBinding.inflate(inflater, container, false)

        // disable search view in details fragment
        parentActivity.searchView.isIconified = true
        parentActivity.searchView.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.Default).launch {
            moviesViewModel.getMovie(args.imdbID)
        }
        CoroutineScope(Dispatchers.Main).launch{
            if (moviesRepository.getMovie(args.imdbID) == null){
                // movie does not exist in collection
                // show add button
                binding.fab.visibility = View.VISIBLE
            } else {
                // movie exists in collection
                // show remove button
                binding .fabHide.visibility = View.VISIBLE
            }
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
        binding.fab.apply {
            setOnClickListener {
                CoroutineScope(Dispatchers.Default).launch {
                    // save and show message
                    moviesRepository.saveMovie(theMovie!!)
                    Snackbar.make(
                        it,
                        getString(R.string.movie_added),
                        Snackbar.LENGTH_LONG
                    )
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show()
                }
                it.visibility = View.GONE
                binding.fabHide.visibility = View.VISIBLE
            }
        }

        binding.fabHide.apply {
            setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    // remove and show message
                    moviesRepository.deleteMovie(theMovie!!)
                    Snackbar.make(
                        it,
                        getString(R.string.movie_removed),
                        Snackbar.LENGTH_LONG
                    )
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show()
                    findNavController().popBackStack()
                }
                it.visibility = View.GONE
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}