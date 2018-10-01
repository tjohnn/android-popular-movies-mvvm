package com.tjohnn.popularmovies.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.tjohnn.popularmovies.data.dtos.Movie;

@Entity(tableName = "favorite_movie")
public class FavoriteMovie {

    @PrimaryKey(autoGenerate = false)
    public long id;
    public String backdropPath;
    public String posterPath;
    public String title;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public double voteAverage;

    @Ignore
    public FavoriteMovie(Movie movie) {
        id = movie.id;
        backdropPath = movie.backdropPath;
        posterPath = movie.posterPath;
        title = movie.title;
        originalTitle = movie.originalTitle;
        overview = movie.overview;
        releaseDate = movie.releaseDate;
        voteAverage = movie.voteAverage;
    }

    public FavoriteMovie(long id, String backdropPath, String posterPath, String title, String originalTitle, String overview, String releaseDate, double voteAverage) {
        this.id = id;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }
}
