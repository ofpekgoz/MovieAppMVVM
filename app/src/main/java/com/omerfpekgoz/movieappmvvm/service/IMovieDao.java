package com.omerfpekgoz.movieappmvvm.service;

import com.omerfpekgoz.movieappmvvm.model.Movie;
import com.omerfpekgoz.movieappmvvm.response.MovieResponse;
import com.omerfpekgoz.movieappmvvm.response.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMovieDao {

    // https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher

    //Serach Movie
    @GET("3/search/movie")
    Call<MovieResult> searchMovie(@Query("api_key") String api_key,
                                  @Query("query") String query,
                                  @Query("page") int page);


    //https://api.themoviedb.org/3/movie/550?api_key=0307c4edab2bfd30c576d9972a43bf2a
    //Search Moview By Moview Id
    @GET("3/movie/{movie_id}?")
    Call<MovieResponse> getMovieByMovieId(@Path("movie_id") int movie_id,
                                          @Query("api_key") String api_key);

    //Popular Movie
    @GET("3/movie/popular")
    Call<MovieResult> getpopularMovie(@Query("api_key") String api_key,
                                      @Query("page") int page);


}
