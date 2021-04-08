package com.evangelidis.movieramanew.databases

import android.annotation.SuppressLint
import com.evangelidis.movieramanew.debugLog
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class WatchlistRepository(private val watchlistDao: WatchlistDao) : IWatchlistRepository {

    @SuppressLint("CheckResult")
    override fun insert(watchlistMovie: WatchlistData) {
        watchlistDao.insert(watchlistMovie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { debugLog { "Insert Success" } },
                { debugLog { "Insert Error" } }
            )
    }

    @SuppressLint("CheckResult")
    override fun delete(movieId: Int) {
        watchlistDao.deleteMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { debugLog { "Insert Success" } },
                { debugLog { "Insert Error" } }
            )
    }

    override fun getAll(): Observable<List<WatchlistData>>  = watchlistDao.getAll()


}