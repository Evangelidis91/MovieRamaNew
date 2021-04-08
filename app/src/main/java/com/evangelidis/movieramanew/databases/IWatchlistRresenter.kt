package com.evangelidis.movieramanew.databases

import androidx.lifecycle.LiveData

interface IWatchlistRresenter {

    fun insert(watchlistMovie: WatchlistData)

    fun delete(movieId:Int)

    fun getAll(): LiveData<List<WatchlistData>>

    fun loadProducts()
}