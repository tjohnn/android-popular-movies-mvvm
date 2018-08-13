package com.tjohnn.popularmovies.data.repository;

import com.tjohnn.popularmovies.data.model.Movie;
import com.tjohnn.popularmovies.data.model.ArrayResponse;
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

    public Single<ArrayResponse<Movie>> getMoviesByPopularity(boolean forceRefresh){
        return apiService.getMoviesByRating(forceRefresh ? "no-cache" : null);
    }

    public Single<ArrayResponse<Movie>> getMoviesByRating(boolean forceRefresh) {
        return apiService.getMoviesByPopularity(forceRefresh ? "no-cache" : null);
    }

    public Single<Movie> getMovieById(String movieId) {
        return apiService.getMovieById(movieId);
    }
}
