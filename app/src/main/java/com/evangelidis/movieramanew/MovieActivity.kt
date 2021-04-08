package com.evangelidis.movieramanew

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.evangelidis.movierama.show
import com.evangelidis.movierama.then
import com.evangelidis.movierama.updatePadding
import com.evangelidis.movieramanew.Constants.CATEGORY_DIRECTOR
import com.evangelidis.movieramanew.Constants.IMAGE_POSTER_BASE_URL
import com.evangelidis.movieramanew.Constants.IMAGE_SMALL_BASE_URL
import com.evangelidis.movieramanew.Constants.INPUT_DATE_FORMAT
import com.evangelidis.movieramanew.Constants.OUTPUT_DATE_FORMAT
import com.evangelidis.movieramanew.databases.WatchlistData
import com.evangelidis.movieramanew.databases.WatchlistPresenter
import com.evangelidis.movieramanew.databinding.ActivityMovieBinding
import com.evangelidis.movieramanew.databinding.ThumbnailActorsListBinding
import com.evangelidis.movieramanew.databinding.ThumbnailMovieBinding
import com.evangelidis.movieramanew.model.Cast
import com.evangelidis.movieramanew.model.Crew
import com.evangelidis.movieramanew.model.Genre
import com.evangelidis.movieramanew.model.MovieDetailsResponse
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MovieActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID = "MOVIE_ID"

        fun createIntent(context: Context, movieId: Int): Intent =
            Intent(context, MovieActivity::class.java)
                .putExtra(MOVIE_ID, movieId)
    }

    private val binding: ActivityMovieBinding by lazy { ActivityMovieBinding.inflate(layoutInflater) }
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()
    private val movieSimilarViewModel: MovieListViewModel by viewModel()
    private val movieCreditsViewModel: MovieCreditsViewModel by viewModel()
    private val databaseViewModel: WatchlistPresenter by viewModel()

    private var isMovieInWatchlist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val movieId = intent.getIntExtra(MOVIE_ID, 0)

        movieDetailsViewModel.retrieveMovieDetails(movieId)
        movieSimilarViewModel.retrieveSimilarMovieDetails(movieId)
        movieCreditsViewModel.retrieveMovieCredits(movieId)

        observeViewModel()

        binding.backButton.setOnClickListener { finish() }

        observeDatabase(movieId)
    }

    private fun observeDatabase(movieId: Int){
        databaseViewModel.getAll().observe(this, Observer<List<WatchlistData>>{
            val isInWatchlist = it.find { it.itemId == movieId } != null
            isMovieInWatchlist = isInWatchlist
            binding.itemMovieWatchlist.setImageResource(isInWatchlist then { R.drawable.icon_watchlist_filled } ?: R.drawable.icon_watchlist)
        })
    }

    private fun observeViewModel(){
        movieDetailsViewModel.uiState.observe(this, Observer {
            val dataState = it ?: return@Observer

            if (dataState.movie != null && !dataState.movie.consumed) {
                dataState.movie.consume()?.let { movie ->
                    setupUI(movie)
                    setupGenres(movie.genres!!)
                    setWatchlistIcon(movie)
                }
            }

            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { error ->
                    println(error)
                }
            }
        })

        movieSimilarViewModel.uiState.observe(this, Observer {
            val dataState = it ?: return@Observer

            if (dataState.movies != null && !dataState.movies.consumed) {
                dataState.movies.consume()?.let { movies ->
                    setUpSimilarMoviesUI(movies)
                }
            }

            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { error ->
                    println(error)
                }
            }
        })

        movieCreditsViewModel.uiState.observe(this, Observer {
            val dataState = it ?: return@Observer

            if (dataState.credits != null && !dataState.credits.consumed){
                dataState.credits.consume()?.let { credits ->
                    setUpActors(credits.cast)
                    setUpDirectors(credits.crew)
                }
            }

            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { error ->
                    println(error)
                }
            }
        })
    }

    private fun setWatchlistIcon(movie: MovieDetailsResponse) {
        binding.itemMovieWatchlist.setOnClickListener {
            if (isMovieInWatchlist){
                databaseViewModel.delete(movie.id!!)
            } else{
                val watchItem = WatchlistData().apply {
                    itemId = movie.id!!
                    name = movie.title.orEmpty()
                    posterPath = movie.posterPath.orEmpty()
                    releasedDate = movie.releaseDate.orEmpty()
                    rate = movie.voteAverage ?: 0.0
                }
                databaseViewModel.insert(watchItem)
            }
        }
    }

    private fun setUpActors(casts: List<Cast>?) {
        binding.movieActors.removeAllViews()
        casts?.let {
            if (it.isNotEmpty()) {
                for (cast in it) {
                    val item = ThumbnailActorsListBinding.inflate(layoutInflater)
                    getImageTopRadius(this, IMAGE_SMALL_BASE_URL.plus(cast.profilePath), item.thumbnail)
                    item.actorName.text = cast.name
                    item.actorCharacter.text = cast.character
                    item.root.updatePadding(left = 20, right = 20, bottom = 20)
                    binding.movieActors.addView(item.root)
                }
                binding.actorsContainer.show()
            }
        }
    }

    private fun setUpDirectors(crew: List<Crew>?) {
        val directorsList = ArrayList<String>()
        crew?.let { it ->
            for (x in it.indices) {
                if (it[x].job == CATEGORY_DIRECTOR) {
                    it[x].name?.let {
                        directorsList.add(it)
                    }
                }
            }
        }
        if (directorsList.isNotEmpty()) {
            when (directorsList.size) {
                1 -> binding.movieDirectors.text = directorsList[0]
                2 -> binding.movieDirectors.text = getString(R.string.multi_directors)
                    .replace("{dir1}", directorsList[0])
                    .replace("{dir2}", directorsList[1])

                else -> {
                    var directorsString: String? = null
                    for (x in 0 until directorsList.size - 1) {
                        directorsString += directorsList[x] + ", "
                    }
                    directorsString = directorsString?.substring(0, directorsString.length - 2)
                    directorsString += "and " + directorsList[directorsList.size]

                    binding.movieDirectors.text = directorsString
                }
            }
            binding.directorsContainer.show()
        }
    }

    private fun setupUI(movie: MovieDetailsResponse) {
        if (!movie.backdropPath.isNullOrEmpty()) {
            Glide.with(this)
                .load(IMAGE_POSTER_BASE_URL.plus(movie.backdropPath))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.moviePoster)
        }

        binding.movieTitle.text = movie.title
        binding.movieRate.clearLayout()
        binding.movieRate.setRatings(movie.voteAverage?.div(2) ?: 0.0, resources.getDimension(R.dimen.rating_star_large_size).toInt())
        binding.movieRating.text = movie.voteAverage.toString()

        movie.releaseDate?.let {
            binding.movieReleaseDate.text = changeDateFormat(it)
            binding.movieReleaseDate.show()
        }

        movie.runtime?.let {
            if (it > 0) {
                binding.movieDuration.text = formatHoursAndMinutes(it)
                binding.movieDuration.show()
            }
        }

        movie.overview?.let {
            binding.movieDetailsOverview.text = it
            binding.summaryContainer.show()
        }

    }

    private fun setUpSimilarMoviesUI(data: List<Movie>) {
        binding.movieSimilar.removeAllViews()
        data.let {
            if (it.isNotEmpty()) {
                for (similarResult in it) {
                    val item = ThumbnailMovieBinding.inflate(layoutInflater)
                    getImageTopRadius(this, IMAGE_SMALL_BASE_URL.plus(similarResult.posterPath), item.thumbnail)
                    item.movieName.text = similarResult.title
                    item.movieRate.text = similarResult.voteAverage.toString()
                    item.thumbnail.setOnClickListener {
                        startActivity(createIntent(this, similarResult.id))
                    }
                    item.root.updatePadding(left = 20, right = 20, bottom = 20)
                    binding.movieSimilar.addView(item.root)
                }
                binding.similarMoviesContainer.show()
            }
        }
    }

    fun getImageTopRadius(context: Context, url: String, target: ImageView) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transform(CenterInside(), GranularRoundedCorners(context.resources.getDimension(R.dimen.image_top_radius), context.resources.getDimension(R.dimen.image_top_radius), 0f, 0f))
            .skipMemoryCache(true)
            .into(target)
    }

    private fun setupGenres(genre: List<Genre>) {
        val genres: ArrayList<String> = arrayListOf()
        for (element in genre) {
            genres.add(element.name.toString())
        }
        if (genres.isNotEmpty()) {
            binding.movieGenres.text = genres.joinToString(separator = ", ")
            binding.movieGenres.show()
        }
    }

    private fun changeDateFormat(date: String): String {
        val parser = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.UK)
        val formatter = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.UK)
        return formatter.format(parser.parse(date))
    }

    private fun formatHoursAndMinutes(totalMinutes: Int): String {
        val hours = (totalMinutes / 60).toString()
        val minutes = (totalMinutes % 60).toString()
        return resources.getString(R.string.hour_format)
            .replace("{hour}", (hours)) + getString(R.string.minutes_format)
            .replace("{min}", minutes)
    }
}