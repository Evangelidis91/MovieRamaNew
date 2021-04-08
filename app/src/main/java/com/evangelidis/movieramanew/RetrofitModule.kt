package com.evangelidis.movieramanew

import com.evangelidis.movieramanew.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient

val retrofitModule = module {

    single<Call.Factory> {
        okhttp()
    }

    single {
        retrofit(get(), BASE_URL)
    }

    single {
        get<Retrofit>().create(TMDBApi::class.java)
    }
}

private val interceptor: Interceptor
    get() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


private fun retrofit(callFactory: Call.Factory, baseUrl: String) = Retrofit.Builder()
    .callFactory(callFactory)
    .baseUrl(baseUrl)
    .addConverterFactory(
        Json(
            JsonConfiguration(strictMode = false)
        ).asConverterFactory("application/json".toMediaType())
    )
    .build()

private fun okhttp() = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build()