package com.evangelidis.movieramanew.databases

import io.reactivex.Observable

interface IWatchlistRepository {

    fun insert(watchlistMovie: WatchlistData)

    fun delete(movieId: Int)

    fun getAll(): Observable<List<WatchlistData>>

}