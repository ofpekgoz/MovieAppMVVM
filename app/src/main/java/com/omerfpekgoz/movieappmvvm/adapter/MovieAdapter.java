package com.omerfpekgoz.movieappmvvm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.omerfpekgoz.movieappmvvm.R;
import com.omerfpekgoz.movieappmvvm.databinding.CardviewMovieitemBinding;
import com.omerfpekgoz.movieappmvvm.fragment.MovieListFragmentDirections;
import com.omerfpekgoz.movieappmvvm.model.Movie;
import com.omerfpekgoz.movieappmvvm.network.APIUtils;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.cardViewHolder> {

    private Context mContext;
    private View view;
    private List<Movie> movieList;
    private CardviewMovieitemBinding movieitemBinding;

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        movieitemBinding = CardviewMovieitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        view = movieitemBinding.getRoot();
        mContext = view.getContext();
        return new MovieAdapter.cardViewHolder(movieitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        movieitemBinding.txtMovieTitle.setText(movie.getMovieTitle());
        movieitemBinding.txtMovieRealiseDate.setText(movie.getMovieReleaseDate());
        movieitemBinding.txtMovieLanguage.setText(movie.getOriginal_language().toUpperCase());

        //Gelen veride 10 üzerinden olduğu için 2 ye böldük
        movieitemBinding.ratingBar.setRating(movie.getMovieVoteAverage() / 2);

        //Glide
        Glide.with(mContext).load(APIUtils.IMAGE_URL + movie.getMoviePosterPath()).centerCrop().placeholder(R.drawable.ic_launcher_background).into(movieitemBinding.imgMovie);

        movieitemBinding.carViewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action =
                        MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie.getMovieId());
                        Navigation.findNavController(view).navigate(action);
            }
        });


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class cardViewHolder extends RecyclerView.ViewHolder {

        CardviewMovieitemBinding movieitemBinding;

        public cardViewHolder(CardviewMovieitemBinding movieitemBinding) {
            super(movieitemBinding.getRoot());
            this.movieitemBinding = movieitemBinding;

        }
    }
}
