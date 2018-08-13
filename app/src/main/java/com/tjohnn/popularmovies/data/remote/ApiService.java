package com.tjohnn.popularmovies.data.remote;

import com.tjohnn.popularmovies.data.model.ArrayResponse;
import com.tjohnn.popularmovies.data.model.Movie;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {

    @GET("top_rated")
    Single<ArrayResponse<Movie>> getMoviesByRating(@Header("Cache-Control") String cacheRule);

    @GET("popular")
    Single<ArrayResponse<Movie>> getMoviesByPopularity(@Header("Cache-Control") String cacheRule);

    @GET("{movieId}")
    Single<Movie> getMovieById(@Path("movieId") String movieId);
}
