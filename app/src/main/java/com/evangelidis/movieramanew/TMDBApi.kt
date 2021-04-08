package com.evangelidis.movieramanew


import com.evangelidis.movieramanew.model.MovieCreditsResponse
import com.evangelidis.movieramanew.model.MovieDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

//    @GET("genre/movie/list")
//    fun getMoviesGenres(
//        @Query("api_key") apiKey: String
//    ): GenresResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key", encoded = false) apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MoviesListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): MovieCreditsResponse


    @GET("movie/{movie_id}/similar")
    suspend fun getMovieSimilar(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): MoviesListResponse
//
//    @GET("search/movie")
//    fun getMoviesSearchResult(
//        @Query("api_key") apiKey: String,
//        @Query("query") query: String,
//        @Query("page") page: Int,
//        @Query("language") language: String
//    ): MoviesListResponse
}