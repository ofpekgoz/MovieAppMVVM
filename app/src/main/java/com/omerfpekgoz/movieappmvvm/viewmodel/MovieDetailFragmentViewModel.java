package com.omerfpekgoz.movieappmvvm.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.omerfpekgoz.movieappmvvm.model.Movie;
import com.omerfpekgoz.movieappmvvm.network.APIUtils;
import com.omerfpekgoz.movieappmvvm.response.MovieResponse;
import com.omerfpekgoz.movieappmvvm.response.MovieResult;
import com.omerfpekgoz.movieappmvvm.service.IMovieDao;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragmentViewModel extends ViewModel {

    private MutableLiveData<MovieResponse> movieResponseLiveData;
    IMovieDao movieDao;

    public MovieDetailFragmentViewModel() {
        movieResponseLiveData = new MutableLiveData<>();
        movieDao = APIUtils.getMovieDao();
    }

    public LiveData<MovieResponse> getMovieByMovieId(String api_key, int movie_id) {
        movieDao.getMovieByMovieId(movie_id, api_key).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                MovieResponse movieResponse = response.body();
                movieResponseLiveData.postValue(movieResponse);


            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

        return movieResponseLiveData;
    }
}
