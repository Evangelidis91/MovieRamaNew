package com.evangelidis.movieramanew.databases

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

    single { Room.databaseBuilder(get(), WatchlistDatabase::class.java, "app.db").build() }

    single { get<WatchlistDatabase>().watchlistDao() }

    single<IWatchlistRepository> {
        WatchlistRepository(get())
    }

    single<IWatchlistInteractor> {
        WatchlistInteractor(get())
    }

    viewModel { WatchlistPresenter(get()) }
}