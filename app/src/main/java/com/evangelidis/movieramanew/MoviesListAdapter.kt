package com.evangelidis.movieramanew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evangelidis.movieramanew.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MoviesListAdapter(private var callback: MovieListenerCallback, private val picasso: Picasso) : RecyclerView.Adapter<MoviesViewHolder>() {

    var moviesListData = mutableListOf<Movie>()
        set(value) {
            moviesListData.clear()
            moviesListData.addAll(value)
            notifyDataSetChanged()
        }

//    var watchlistList = mutableListOf<WatchlistData>()
//        set(value) {
//            watchlistList.clear()
//            watchlistList.addAll(value)
//            notifyDataSetChanged()
//        }

    var newData = mutableListOf<Movie>()
        set(value) {
            moviesListData.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = moviesListData.count()

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(moviesListData[position], callback, picasso)
    }
}

interface MovieListenerCallback {
    fun navigateToMovie(movieId: Int)
}