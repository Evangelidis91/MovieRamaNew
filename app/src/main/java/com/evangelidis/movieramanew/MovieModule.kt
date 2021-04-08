package com.evangelidis.movieramanew

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieModule = module {

    viewModel {
        MovieViewModel(get())
    }
}