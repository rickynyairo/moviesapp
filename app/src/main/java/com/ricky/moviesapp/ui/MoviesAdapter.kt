package com.ricky.moviesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ricky.moviesapp.entity.Movie
import com.ricky.moviesapp.R
import com.ricky.moviesapp.databinding.MovieItemBinding
import com.squareup.picasso.Picasso

class MoviesAdapter(private var moviesList: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.bind(movie)
    }

    override fun getItemCount() = moviesList.size

    fun submitList(movies: List<Movie>) {
        moviesList = movies
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.titleTextView.text = movie.title
            binding.yearTextView.text = movie.year
            binding.typeTextView.text = movie.type
            Picasso.get().load(movie.posterUrl).placeholder(R.drawable.placeholder)
                .into(binding.posterImageView)

        }
    }
}
