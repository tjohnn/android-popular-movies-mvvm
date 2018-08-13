package com.tjohnn.popularmovies.ui.movies;

import com.tjohnn.popularmovies.data.model.ArrayResponse;
import com.tjohnn.popularmovies.data.model.Movie;


public class MoviesUiModel {

    private final boolean isMoviesGridVisible;
    private final ArrayResponse<Movie> movieList;

    MoviesUiModel(boolean isMoviesGridVisible, ArrayResponse<Movie> movieList){

        this.isMoviesGridVisible = isMoviesGridVisible;
        this.movieList = movieList;
    }

    public boolean isMoviesGridVisible() {
        return isMoviesGridVisible;
    }

    public ArrayResponse<Movie> getMovieList() {
        return movieList;
    }

    @Override
    public String toString() {
        return "MoviesUiModel{" +
                "isMoviesGridVisible=" + isMoviesGridVisible +
                ", movieList=" + movieList +
                '}';
    }
}
