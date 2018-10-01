package com.tjohnn.popularmovies.data.repository;

import com.tjohnn.popularmovies.data.dtos.Movie;
import com.tjohnn.popularmovies.data.dtos.ArrayResponse;
import com.tjohnn.popularmovies.data.dtos.Review;
import com.tjohnn.popularmovies.data.dtos.Trailer;
import com.tjohnn.popularmovies.data.remote.ApiService;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class MoviesRepository {

    private ApiService apiService;

    @Inject
    public MoviesRepository(ApiService apiService){

        this.apiService = apiService;
    }

    public Single<ArrayResponse<Movie>> getMoviesByPopularity(boolean forceRefresh, int page){
        return apiService.getMoviesByRating(forceRefresh ? "no-cache" : null, page);
    }

    public Single<ArrayResponse<Movie>> getMoviesByRating(boolean forceRefresh, int page) {
        return apiService.getMoviesByPopularity(forceRefresh ? "no-cache" : null, page);
    }

    public Single<Movie> getMovieById(String movieId) {
        return apiService.getMovieById(movieId);
    }

    public Single<ArrayResponse<Trailer>> getMovieTrailers(String movieId) {
        return apiService.getMovieTrailers(movieId);
    }
    public Single<ArrayResponse<Review>> getMovieReviews(String movieId) {
        return apiService.getMovieReviews(movieId);
    }
}
