package com.omerfpekgoz.movieappmvvm.repositories;

import android.renderscript.ScriptGroup;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omerfpekgoz.movieappmvvm.model.Movie;
import com.omerfpekgoz.movieappmvvm.network.APIUtils;

import com.omerfpekgoz.movieappmvvm.response.MovieResponse;
import com.omerfpekgoz.movieappmvvm.service.IMovieDao;

import java.util.List;


public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPage;

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository() {

        movieApiClient = MovieApiClient.getInstance();
    }


    public LiveData<List<MovieResponse>> getMovieList() {
        return movieApiClient.getMovieList();
    }


    //2)Calling the method in repository
    public void searchMovieApi(String query, int page) {
        mQuery = query;
        mPage = page;
        movieApiClient.searchMoviesApi(query, page);

    }

    public void searchNextPage() {
        searchMovieApi(mQuery, mPage + 1);
    }

}
