package com.ricky.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ricky.moviesapp.MainActivity
import com.ricky.moviesapp.api.OmdbApiClient
import com.ricky.moviesapp.databinding.MoviesListFragmentBinding
import com.ricky.moviesapp.entity.Movie
import com.ricky.moviesapp.persistence.DatabaseProvider
import com.ricky.moviesapp.persistence.MoviesRepository
import com.ricky.moviesapp.ui.MoviesAdapter.OnItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MoviesListFragment : Fragment(), OnItemClickListener {

    private var _binding: MoviesListFragmentBinding? = null
    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var parentActivity: FragmentActivity

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val moviesRepository = MoviesRepository(
            OmdbApiClient(),
            DatabaseProvider.getMoviesDao()
        )
        parentActivity = requireActivity() as MainActivity
        moviesViewModel = ViewModelProvider(
            parentActivity,
            MoviesViewModelFactory(moviesRepository))[MoviesViewModel::class.java]

        _binding = MoviesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // replace with a call to the view model.
        moviesAdapter = MoviesAdapter(emptyList(), this)
        moviesViewModel.movies.observe(viewLifecycleOwner) { movies ->
            if(!movies.isNullOrEmpty()){
                moviesAdapter.submitList(movies)
            }
            else{
                // movies is null or empty, error from the response
                // ignore results
            }
        }
        binding.savedMoviesRecyclerView.adapter = moviesAdapter
        CoroutineScope(Dispatchers.Default).launch {
            moviesViewModel.getSavedMovies()
        }
    }

    override fun onItemClick(movie: Movie, view: View) {
        Navigation
            .findNavController(view)
            .navigate(
                MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movie.imdbID)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}