package com.ricky.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ricky.moviesapp.api.OmdbApiClient
import com.ricky.moviesapp.databinding.MoviesListFragmentBinding
import com.ricky.moviesapp.persistence.MoviesRepository

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MoviesListFragment : Fragment() {

    private var _binding: MoviesListFragmentBinding? = null
    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        moviesViewModel = MoviesViewModel(MoviesRepository(OmdbApiClient()))
        _binding = MoviesListFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // replace with a call to the view model.
        moviesAdapter = MoviesAdapter(emptyList())
        moviesViewModel.movies.observe(viewLifecycleOwner) { movies ->
            moviesAdapter.submitList(movies)
        }
        binding.savedMoviesRecyclerView.adapter = moviesAdapter
        moviesViewModel.searchMovies("The Godfather")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}