package com.evangelidis.movieramanew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evangelidis.movieramanew.databases.WatchlistData
import com.evangelidis.movieramanew.databases.WatchlistPresenter
import com.evangelidis.movieramanew.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MovieListenerCallback {

    private val picasso: Picasso by inject()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val movieViewModel: MovieListViewModel by viewModel()
    private val databaseViewModel: WatchlistPresenter by viewModel()
    private val moviesListAdapter = MoviesListAdapter(this, picasso)
    private var listOfRetrievedPages = arrayListOf(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        movieViewModel.retrieveMovies(1)

        binding.moviesList.layoutManager = LinearLayoutManager(this)
        binding.moviesList.adapter = moviesListAdapter

        observeViewModel()
        setUpScrollListener()
    }

    override fun onResume() {
        super.onResume()
        databaseViewModel.getAll().observe(this, Observer<List<WatchlistData>>{
            moviesListAdapter.watchlistList = it as MutableList<WatchlistData>
        })
    }

    private fun observeViewModel() {

        movieViewModel.uiState.observe(this, Observer {
            val dataState = it ?: return@Observer

            if (dataState.movies != null && !dataState.movies.consumed) {
                dataState.movies.consume()?.let { movies ->
                    if (listOfRetrievedPages.size == 1) {
                        moviesListAdapter.moviesListData = movies as MutableList<Movie>
                    } else {
                        moviesListAdapter.newData = movies as MutableList<Movie>
                    }
                }
            }
            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { error ->

                }
            }
        })
    }

    private fun setUpScrollListener() {
        val movieManager = LinearLayoutManager(this)
        binding.moviesList.layoutManager = movieManager
        binding.moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = movieManager.itemCount
                val visibleItemCount = movieManager.childCount
                val firstVisibleItem = movieManager.findFirstVisibleItemPosition()
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    listOfRetrievedPages.add(listOfRetrievedPages.last() + 1)
                    movieViewModel.retrieveMovies(listOfRetrievedPages.last())
                }
            }
        })
    }

    override fun navigateToMovie(movieId: Int) {
        startActivity(MovieActivity.createIntent(this, movieId))
    }

    override fun insertToWatchlist(watchlistData: WatchlistData) {
        databaseViewModel.insert(watchlistData)
    }

    override fun removeFromWatchlist(movieId: Int) {
        databaseViewModel.delete(movieId)
    }
}