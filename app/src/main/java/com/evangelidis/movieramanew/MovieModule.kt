package com.evangelidis.movieramanew

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieListModule = module {

    viewModel {
        MovieListViewModel(get())
    }
}