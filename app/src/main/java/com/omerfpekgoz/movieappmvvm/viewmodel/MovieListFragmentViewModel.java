package com.omerfpekgoz.movieappmvvm.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.omerfpekgoz.movieappmvvm.model.Movie;
import com.omerfpekgoz.movieappmvvm.network.APIUtils;
import com.omerfpekgoz.movieappmvvm.repositories.MovieRepository;
import com.omerfpekgoz.movieappmvvm.response.MovieResponse;
import com.omerfpekgoz.movieappmvvm.response.MovieResult;
import com.omerfpekgoz.movieappmvvm.service.IMovieDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragmentViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<List<MovieResponse>> popularMovieList;
    private IMovieDao movieDao;

    int mPage;


    public MovieListFragmentViewModel() {
        movieRepository = MovieRepository.getInstance();
        popularMovieList = new MutableLiveData<>();
        movieDao = APIUtils.getMovieDao();
    }

    public LiveData<List<MovieResponse>> getMovieList() {
        return movieRepository.getMovieList();
    }


    //3)Calling the method in viewmodel

    public void searchMovieApi(String query, int page) {
        movieRepository.searchMovieApi(query, page);
    }

    public void searchNextPage() {
        movieRepository.searchNextPage();
    }

    public LiveData<List<MovieResponse>> getPopularMovie(int page) {

        mPage = page;
        movieDao.getpopularMovie(APIUtils.API_KEY, page).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult movieResult = response.body();
                List<MovieResponse> movieResponseList = movieResult.getMovieResponse();
                popularMovieList.postValue(movieResponseList);

            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });

        return popularMovieList;
    }

    public void getPopularMovieNextPage() {
        getPopularMovie(mPage + 1);
    }

}
