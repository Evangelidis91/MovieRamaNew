package com.evangelidis.movieramanew.databases

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WatchlistData::class], version = 1)
abstract class WatchlistDatabase : RoomDatabase() {

    abstract fun watchlistDao() : WatchlistDao
}