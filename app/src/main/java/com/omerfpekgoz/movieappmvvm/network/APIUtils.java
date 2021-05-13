
package com.omerfpekgoz.movieappmvvm.network;


import com.omerfpekgoz.movieappmvvm.service.IMovieDao;

public class APIUtils {


    //https://api.themoviedb.org/3/movie/550?api_key=0307c4edab2bfd30c576d9972a43bf2a

    //Pages
    //https://api.themoviedb.org/3/movie/popular?api_key=0307c4edab2bfd30c576d9972a43bf2a&page=1

    //Search
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher

    //Detail
    //https://api.themoviedb.org/3/movie/343611?api_key={api_key}

    //Images
    //https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg


    public static final String BASE_URL = "https://api.themoviedb.org/";

    public static final String API_KEY = "0307c4edab2bfd30c576d9972a43bf2a";

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";


    public static IMovieDao getMovieDao() {

        return RetrofitClient.getClient(BASE_URL).create(IMovieDao.class);

    }

}
