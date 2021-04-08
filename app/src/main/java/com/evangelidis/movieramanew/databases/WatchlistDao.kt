package com.evangelidis.movieramanew.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlistData")
    fun getAll(): Observable<List<WatchlistData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(watchlistData: WatchlistData): Completable

    @Query("DELETE FROM watchlistData WHERE itemId = :itemId")
    fun deleteMovie(itemId: Int): Completable

//    @Query("SELECT * FROM watchlistData WHERE itemId = :itemId")
//    fun getItem(itemId: Int): WatchlistData
}