package com.evangelidis.movieramanew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evangelidis.movieramanew.Constants.API_KEY
import com.evangelidis.movieramanew.Constants.LANGUAGE
import kotlinx.coroutines.launch

class MovieListViewModel constructor(private val serviceUtil: TMDBApi) : ViewModel() {

    private val _uiState = MutableLiveData<MovieDataState>()
    val uiState: LiveData<MovieDataState> get() = _uiState

    fun retrieveMovies(pageNumber: Int) {
        viewModelScope.launch {
            runCatching {
                emitUiState(showProgress = true)
                serviceUtil.getPopularMovies(apiKey = API_KEY, page = pageNumber, language = LANGUAGE)
            }.onSuccess {
                emitUiState(movies = Event(it.results))
            }.onFailure {
                emitUiState(error = Event(R.string.internet_failure_error))
            }
        }
    }

    fun retrieveSimilarMovieDetails(movieId: Int) {
        viewModelScope.launch {
            runCatching {
                emitUiState(showProgress = true)
                serviceUtil.getMovieSimilar(movieId, API_KEY, LANGUAGE)
            }.onSuccess {
                emitUiState(movies = Event(it.results))
            }.onFailure {
                emitUiState(error = Event(R.string.internet_failure_error))
            }
        }
    }

    private fun emitUiState(showProgress: Boolean = false, movies: Event<List<Movie>>? = null, error: Event<Int>? = null) {
        val dataState = MovieDataState(showProgress, movies, error)
        _uiState.value = dataState
    }
}

data class MovieDataState(
    val showProgress: Boolean,
    val movies: Event<List<Movie>>?,
    val error: Event<Int>?
)