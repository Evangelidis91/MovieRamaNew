package com.evangelidis.movieramanew

import androidx.recyclerview.widget.RecyclerView
import com.evangelidis.movieramanew.Constants.IMAGE_POSTER_BASE_URL
import com.evangelidis.movieramanew.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MoviesViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, callback: MovieListenerCallback, picasso: Picasso) {

        //binding.watchListIcon.setImageResource((watchlistList.find { it.itemId == movie.id } != null) then { R.drawable.icon_watchlist_filled } ?: R.drawable.icon_watchlist)
        binding.movieTitle.text = movie.originalTitle
        binding.movieReleaseDate.text = movie.releaseDate
        binding.rating.apply {
            clearLayout()
            setRatings(
                movie.voteAverage?.div(2) ?: 0.0,
                resources.getDimension(R.dimen.rating_star_large_size).toInt()
            )
        }

        picasso.load(IMAGE_POSTER_BASE_URL.plus(movie.backdropPath))
            .into(binding.moviePoster)
//
//        Glide.with(binding.root.context)
//            .load(IMAGE_POSTER_BASE_URL.plus(movie.backdropPath))
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .skipMemoryCache(true)
//            .into(binding.moviePoster)

        binding.root.setOnClickListener { callback.navigateToMovie(movie.id) }
//        binding.watchListIcon.setOnClickListener {
//            val watchItem = WatchlistData().apply {
//                itemId = movie.id
//                name = movie.title.orEmpty()
//                posterPath = movie.posterPath.orEmpty()
//                releasedDate = movie.releaseDate.orEmpty()
//                rate = movie.voteAverage ?: 0.0
//            }
//            if (watchlistList.isNullOrEmpty()) {
//                DatabaseQueries.saveItem(binding.root.context, watchItem)
//                watchlistList.add(watchItem)
//                binding.watchListIcon.setImageResource(R.drawable.icon_watchlist_filled)
//            } else {
//                if (watchlistList.find { it.itemId == movie.id } != null) {
//                    binding.watchListIcon.setImageResource(R.drawable.icon_watchlist)
//                    DatabaseQueries.removeItem(binding.root.context, watchItem.itemId)
//                    watchlistList.remove(watchItem)
//                } else {
//                    DatabaseQueries.saveItem(binding.root.context, watchItem)
//                    watchlistList.add(watchItem)
//                    binding.watchListIcon.setImageResource(R.drawable.icon_watchlist_filled)
//                }
//            }
//        }
    }
}