package com.tjohnn.popularmovies.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.tjohnn.popularmovies.data.dtos.Movie;

import java.util.List;

@Dao
public interface FavoriteMoviesDao {

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    LiveData<FavoriteMovie> getFavoriteMovieById(long id);

    @Transaction
    @Query("SELECT * FROM favorite_movie ORDER BY voteAverage DESC")
    LiveData<List<Movie>> getFavoriteMovies();

    @Insert
    void insertFavoriteMovie(FavoriteMovie favoriteMovie);

    @Update
    void updateFavoriteMovie(FavoriteMovie favoriteMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovie favoriteMovie);

}
