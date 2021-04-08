package com.evangelidis.movieramanew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evangelidis.movieramanew.Constants.API_KEY
import com.evangelidis.movieramanew.Constants.LANGUAGE
import com.evangelidis.movieramanew.model.MovieCreditsResponse
import kotlinx.coroutines.launch

class MovieCreditsViewModel constructor(private val serviceUtils: TMDBApi) : ViewModel() {

    private val _uiState = MutableLiveData<MovieCreditsDataState>()
    val uiState: LiveData<MovieCreditsDataState>
        get() = _uiState

    fun retrieveMovieCredits(movieId:Int){
        viewModelScope.launch {
            runCatching {
                emitUiState(showProgress = true)
                serviceUtils.getMovieCredits(movieId, API_KEY, LANGUAGE)
            }.onSuccess {
                emitUiState(credits = Event(it))
            }.onFailure {
                emitUiState(error = Event(R.string.internet_failure_error))
            }
        }
    }

    private fun emitUiState(showProgress: Boolean = false, credits: Event<MovieCreditsResponse>? = null, error: Event<Int>? = null){
        val dataState = MovieCreditsDataState(showProgress, credits, error)
        _uiState.value = dataState
    }

}

data class MovieCreditsDataState(
    val showProgress: Boolean,
    val credits: Event<MovieCreditsResponse>?,
    val error: Event<Int>?
)