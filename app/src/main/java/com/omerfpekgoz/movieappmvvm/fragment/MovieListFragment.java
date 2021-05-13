package com.omerfpekgoz.movieappmvvm.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.MovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.omerfpekgoz.movieappmvvm.R;
import com.omerfpekgoz.movieappmvvm.adapter.MovieAdapter;
import com.omerfpekgoz.movieappmvvm.databinding.FragmentMovieListBinding;
import com.omerfpekgoz.movieappmvvm.model.Movie;
import com.omerfpekgoz.movieappmvvm.network.APIUtils;
import com.omerfpekgoz.movieappmvvm.response.MovieResponse;
import com.omerfpekgoz.movieappmvvm.response.MovieResult;
import com.omerfpekgoz.movieappmvvm.service.IMovieDao;
import com.omerfpekgoz.movieappmvvm.viewmodel.MovieListFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieListFragment extends Fragment {

    FragmentMovieListBinding movieListBinding;
    MovieAdapter movieAdapter;
    Context mContext;

    IMovieDao movieDao;
    List<MovieResponse> movieResponseList;
    List<Movie> movieList;
    List<Movie> popularMovieList;

    private MovieListFragmentViewModel movieListFragmentViewModel;
    String searchMovieTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieListBinding = FragmentMovieListBinding.inflate(getLayoutInflater());
        View view = movieListBinding.getRoot();
        mContext = view.getContext();

        movieDao = APIUtils.getMovieDao();
        movieResponseList = new ArrayList<>();
        movieList = new ArrayList<>();
        popularMovieList = new ArrayList<>();
        movieListFragmentViewModel = new ViewModelProvider(this).get(MovieListFragmentViewModel.class);


        init();
        getPopularMovie();
        searchMovie();


        movieListBinding.txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchMovieTitle = movieListBinding.txtSearch.getText().toString();

                Log.e("Search Movie", searchMovieTitle.toString());
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    movieList.clear();
                    searchMovieApi(searchMovieTitle, 1);

                }
                return false;
            }
        });

        // getMovies();
        //getMovieByMovieId();


        return view;
    }

    public void init() {
        movieListBinding.recylerViewMovie.setHasFixedSize(true);
        movieListBinding.recylerViewMovie.setLayoutManager(new GridLayoutManager(mContext, 2));


        movieListBinding.recylerViewMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                 if (movieListBinding.recylerViewMovie.canScrollVertically(1)){
//                        movieListFragmentViewModel.getPopularMovieNextPage();
//                 }

            }
        });

    }


    //Search Movie Method
    public void searchMovie() {

        movieListFragmentViewModel.getMovieList().observe(getViewLifecycleOwner(), new Observer<List<MovieResponse>>() {
            @Override
            public void onChanged(List<MovieResponse> movies) {

                //Observing for any data change
                if (movies != null) {
                    for (MovieResponse movieResponse : movies) {
                        //Get data

                        Movie movie = new Movie();
                        movie.setMovieId(movieResponse.getId());
                        movie.setMovieOverview(movieResponse.getOverview());
                        movie.setMovieTitle(movieResponse.getTitle());
                        movie.setMoviePosterPath(movieResponse.getPosterPath());
                        movie.setMovieReleaseDate(movieResponse.getReleaseDate());
                        movie.setMovieVoteAverage(movieResponse.getVoteAverage());
                        movie.setOriginal_language(movieResponse.getOriginalLanguage());

                        movieList.add(movie);

                    }

                    movieAdapter = new MovieAdapter(mContext, movieList);
                    movieListBinding.recylerViewMovie.setAdapter(movieAdapter);

                }

            }
        });
    }

    //Get Popular Movie Method
    public void getPopularMovie() {

        movieListFragmentViewModel.getPopularMovie(1).observe(getViewLifecycleOwner(), new Observer<List<MovieResponse>>() {
            @Override
            public void onChanged(List<MovieResponse> movieResponses) {

                if (movieResponses != null) {
                    for (MovieResponse movieResponse : movieResponses) {
                        Movie movie = new Movie();

                        movie.setMovieId(movieResponse.getId());
                        movie.setMovieOverview(movieResponse.getOverview());
                        movie.setMovieTitle(movieResponse.getTitle());
                        movie.setMoviePosterPath(movieResponse.getPosterPath());
                        movie.setMovieReleaseDate(movieResponse.getReleaseDate());
                        movie.setMovieVoteAverage(movieResponse.getVoteAverage());
                        movie.setOriginal_language(movieResponse.getOriginalLanguage());

                        popularMovieList.add(movie);

                    }
                    movieAdapter = new MovieAdapter(mContext, popularMovieList);
                    movieListBinding.recylerViewMovie.setAdapter(movieAdapter);

                }
            }
        });


    }


    //4)Calling method in Fragment
    private void searchMovieApi(String query, int page) {
        movieListFragmentViewModel.searchMovieApi(query, page);
    }


//
//    private void getMovies() {
//
//        movieDao.searchMovie(APIUtils.API_KEY, "Action", 1).enqueue(new Callback<MovieResult>() {
//            @Override
//            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
//                if (response.code() == 200) {
//
//                    MovieResult movieResult = response.body();
//                    movieResponseList = movieResult.getMovieResponse();
//
//                    Log.e("Response", response.body().toString());
//                    Log.e("movieResponseList", movieResponseList.toString());
//
//                    for (MovieResponse movieResponse : movieResponseList) {
//                        Log.e("MovieResponse", movieResponse.getTitle().toString());
//                    }
//
//                } else {
//
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<MovieResult> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    public void getMovieByMovieId() {
//
//        movieDao.getMovieByMovieId(343611, APIUtils.API_KEY).enqueue(new Callback<Movie>() {
//            @Override
//            public void onResponse(Call<Movie> call, Response<Movie> response) {
//
//                Log.e("Mesaj",response.raw().toString());
//                if (response.code() == 200) {
//                    Movie movie = response.body();
//                    Log.e("MOVİE", movie.getMovieTitle().toString());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Movie> call, Throwable t) {
//
//            }
//        });
//
//
//    }
//

}