package com.tjohnn.popularmovies.ui.moviedetail;

import com.tjohnn.popularmovies.data.dtos.Movie;

public class MovieDetailUiModel {

    private Movie movie;

    public MovieDetailUiModel(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}