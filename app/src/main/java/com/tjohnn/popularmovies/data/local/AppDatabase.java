package com.tjohnn.popularmovies.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {FavoriteMovie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "popular_movies";

    public abstract FavoriteMoviesDao getFavoriteMoviesDao();
}
