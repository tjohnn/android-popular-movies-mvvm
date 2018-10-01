package com.tjohnn.popularmovies.data.remote;

import com.tjohnn.popularmovies.data.dtos.ArrayResponse;
import com.tjohnn.popularmovies.data.dtos.Movie;
import com.tjohnn.popularmovies.data.dtos.Review;
import com.tjohnn.popularmovies.data.dtos.Trailer;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("top_rated")
    Single<ArrayResponse<Movie>> getMoviesByRating(@Header("Cache-Control") String cacheRule, @Query("page") int page);

    @GET("popular")
    Single<ArrayResponse<Movie>> getMoviesByPopularity(@Header("Cache-Control") String cacheRule, @Query("page") int page);

    @GET("{movieId}")
    Single<Movie> getMovieById(@Path("movieId") String movieId);


    @GET("{movieId}/reviews")
    Single<ArrayResponse<Review>> getMovieReviews(@Path("movieId") String movieId);

    @GET("{movieId}/videos")
    Single<ArrayResponse<Trailer>> getMovieTrailers(@Path("movieId") String movieId);
}
