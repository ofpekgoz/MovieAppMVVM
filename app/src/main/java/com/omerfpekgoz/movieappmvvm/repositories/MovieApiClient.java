package com.omerfpekgoz.movieappmvvm.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omerfpekgoz.movieappmvvm.utils.AppExecutors;
import com.omerfpekgoz.movieappmvvm.network.APIUtils;
import com.omerfpekgoz.movieappmvvm.response.MovieResponse;
import com.omerfpekgoz.movieappmvvm.response.MovieResult;
import com.omerfpekgoz.movieappmvvm.service.IMovieDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {


    private MutableLiveData<List<MovieResponse>> movieListLiveData;
    private static MovieApiClient instance;
    private IMovieDao movieDao;
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    public MovieApiClient() {
        movieListLiveData = new MutableLiveData<>();
        movieDao = APIUtils.getMovieDao();
    }


    public LiveData<List<MovieResponse>> getMovieList() {

        return movieListLiveData;
    }


    //1)This method that we are going to call through the classes
    public void searchMoviesApi(String query, int page) {

        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, page);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {

                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int page;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int page) {
            this.query = query;
            this.page = page;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {
                Response response = getMovie(query, page).execute();

                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<MovieResponse> movieList = new ArrayList(((MovieResult) response.body()).getMovieResponse());
                    if (page == 1) {
                        //Sending Data to Live Data
                        movieListLiveData.postValue(movieList);
                    } else {
                        List<MovieResponse> currentMovies = movieListLiveData.getValue();
                        currentMovies.addAll(movieList);
                        movieListLiveData.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().toString();
                    Log.e("Error", error);
                    movieListLiveData.postValue(null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                movieListLiveData.postValue(null);

            }

        }

        //Search Method
        private Call<MovieResult> getMovie(String query, int page) {
            return movieDao.searchMovie(APIUtils.API_KEY, query, page);
        }


        private void cancelRequest() {
            Log.e("Cancel", "Search Request");
            cancelRequest = true;
        }

    }

}
