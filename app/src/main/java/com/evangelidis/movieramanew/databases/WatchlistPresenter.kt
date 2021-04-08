package com.evangelidis.movieramanew.databases

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evangelidis.movieramanew.debugLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WatchlistPresenter(private val iWatchlistInteractor: IWatchlistInteractor) : IWatchlistRresenter, ViewModel() {

    private val allWatchlistMovies: MutableLiveData<List<WatchlistData>> = MutableLiveData()

    init {
        loadProducts()
    }

    override fun insert(watchlistMovie: WatchlistData) {
        iWatchlistInteractor.insert(watchlistMovie)
    }

    override fun delete(movieId: Int) {
        iWatchlistInteractor.delete(movieId)
    }

    override fun getAll(): LiveData<List<WatchlistData>> = allWatchlistMovies

    @SuppressLint("CheckResult")
    override fun loadProducts() {
        iWatchlistInteractor.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { products -> allWatchlistMovies.postValue(products) },
                { debugLog { "Error getting info from interactor into presenter" } }
            )
    }


}