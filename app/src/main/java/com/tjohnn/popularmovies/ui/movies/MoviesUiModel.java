package com.tjohnn.popularmovies.ui.movies;

import com.tjohnn.popularmovies.data.dtos.Movie;

import java.util.List;


public class MoviesUiModel {

    private final boolean isMoviesGridVisible;
    private final List<Movie> movieList;

    MoviesUiModel(boolean isMoviesGridVisible, List<Movie> movieList){

        this.isMoviesGridVisible = isMoviesGridVisible;
        this.movieList = movieList;
    }

    public boolean isMoviesGridVisible() {
        return isMoviesGridVisible;
    }

    public List<Movie> getMovieList() {
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
