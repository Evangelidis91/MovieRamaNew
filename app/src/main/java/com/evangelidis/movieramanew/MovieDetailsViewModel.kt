package com.evangelidis.movieramanew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evangelidis.movieramanew.Constants.API_KEY
import com.evangelidis.movieramanew.Constants.LANGUAGE
import com.evangelidis.movieramanew.model.MovieDetailsResponse
import kotlinx.coroutines.launch

class MovieDetailsViewModel constructor(private val serviceUtil: TMDBApi) : ViewModel() {

    private val _uiState = MutableLiveData<MovieDetailsDataState>()
    val uiState: LiveData<MovieDetailsDataState>
        get() = _uiState

    fun retrieveMovieDetails(movieId: Int) {
        viewModelScope.launch {
            runCatching {
                emitUiState(showProgress = true)
                serviceUtil.getMovieDetails(movieId, API_KEY, LANGUAGE)
            }.onSuccess {
                emitUiState(movie = Event(it))
            }.onFailure {
                emitUiState(error = Event(R.string.internet_failure_error))
            }
        }
    }

    private fun emitUiState(showProgress: Boolean = false, movie: Event<MovieDetailsResponse>? = null, error: Event<Int>? = null){
        val dataState = MovieDetailsDataState(showProgress, movie, error)
        _uiState.value = dataState
    }
}

data class MovieDetailsDataState(
    val showProgress: Boolean,
    val movie: Event<MovieDetailsResponse>?,
    val error: Event<Int>?
)