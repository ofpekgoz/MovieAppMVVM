package com.omerfpekgoz.movieappmvvm.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.omerfpekgoz.movieappmvvm.R;
import com.omerfpekgoz.movieappmvvm.databinding.FragmentMovieDetailBinding;
import com.omerfpekgoz.movieappmvvm.model.Movie;
import com.omerfpekgoz.movieappmvvm.network.APIUtils;
import com.omerfpekgoz.movieappmvvm.response.MovieResponse;
import com.omerfpekgoz.movieappmvvm.viewmodel.MovieDetailFragmentViewModel;

public class MovieDetailFragment extends Fragment {

    FragmentMovieDetailBinding movieDetailBinding;
    Context mContext;
    MovieDetailFragmentViewModel movieDetailFragmentViewModel;


    int movieId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movieDetailBinding = FragmentMovieDetailBinding.inflate(getLayoutInflater());
        View view = movieDetailBinding.getRoot();
        mContext = view.getContext();

        movieDetailFragmentViewModel = new ViewModelProvider(this).get(MovieDetailFragmentViewModel.class);
        movieId = getArguments().getInt("movie_id");

        getMovieByMovieId();

        return view;
    }


    public void getMovieByMovieId() {

        movieDetailFragmentViewModel.getMovieByMovieId(APIUtils.API_KEY, movieId).observe(getViewLifecycleOwner(), new Observer<MovieResponse>() {
            @Override
            public void onChanged(MovieResponse movieResponse) {
                Movie movie = new Movie();
                movie.setMovieTitle(movieResponse.getTitle());
                movie.setBackdrop_path(movieResponse.getBackdropPath());
                movie.setMovieVoteAverage(movieResponse.getVoteAverage());
                movie.setMovieOverview(movieResponse.getOverview());

                setMovieData(movie);
            }
        });
    }

    public void setMovieData(Movie movie) {
        movieDetailBinding.txtMovieTitleDetail.setText(movie.getMovieTitle());
        movieDetailBinding.txtMovieOverviewDetail.setText(movie.getMovieOverview());
        movieDetailBinding.ratingBarDetail.setRating(movie.getMovieVoteAverage() / 2);
        //Glide
        Glide.with(mContext).load(APIUtils.IMAGE_URL + movie.getBackdrop_path()).centerCrop().placeholder(R.drawable.ic_launcher_background).into(movieDetailBinding.imageMovieDetail);
    }
}