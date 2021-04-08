package com.evangelidis.movieramanew.databases

import io.reactivex.Observable

class WatchlistInteractor(private val iWatchlistRepository: IWatchlistRepository) : IWatchlistInteractor {

    override fun insert(watchlistMovie: WatchlistData) {
        iWatchlistRepository.insert(watchlistMovie)
    }

    override fun delete(movieId: Int) {
        iWatchlistRepository.delete(movieId)
    }

    override fun getAll(): Observable<List<WatchlistData>> = iWatchlistRepository.getAll()

}