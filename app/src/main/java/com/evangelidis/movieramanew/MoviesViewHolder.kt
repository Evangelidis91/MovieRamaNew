package com.evangelidis.movieramanew

import androidx.recyclerview.widget.RecyclerView
import com.evangelidis.movierama.then
import com.evangelidis.movieramanew.Constants.IMAGE_POSTER_BASE_URL
import com.evangelidis.movieramanew.databases.WatchlistData
import com.evangelidis.movieramanew.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MoviesViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, isInWatchlist: Boolean, callback: MovieListenerCallback, picasso: Picasso) {

        binding.watchListIcon.setImageResource(isInWatchlist then { R.drawable.icon_watchlist_filled } ?: R.drawable.icon_watchlist)
        binding.movieTitle.text = movie.originalTitle
        binding.movieReleaseDate.text = movie.releaseDate
        binding.rating.apply {
            clearLayout()
            setRatings(movie.voteAverage?.div(2) ?: 0.0, resources.getDimension(R.dimen.rating_star_large_size).toInt())
        }

        picasso.load(IMAGE_POSTER_BASE_URL.plus(movie.backdropPath))
            .into(binding.moviePoster)

        binding.watchListIcon.setOnClickListener {
            if (isInWatchlist) {
                callback.removeFromWatchlist(movie.id)
            } else {
                val watchItem = WatchlistData().apply {
                    itemId = movie.id
                    name = movie.title.orEmpty()
                    posterPath = movie.posterPath.orEmpty()
                    releasedDate = movie.releaseDate.orEmpty()
                    rate = movie.voteAverage ?: 0.0
                }
                callback.insertToWatchlist(watchItem)
            }
        }

//        Glide.with(binding.root.context)
//            .load(IMAGE_POSTER_BASE_URL.plus(movie.backdropPath))
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .skipMemoryCache(true)
//            .into(binding.moviePoster)

        binding.root.setOnClickListener { callback.navigateToMovie(movie.id) }
    }
}