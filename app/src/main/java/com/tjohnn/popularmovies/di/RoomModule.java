package com.tjohnn.popularmovies.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.tjohnn.popularmovies.data.local.AppDatabase;
import com.tjohnn.popularmovies.data.local.FavoriteMoviesDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    @Singleton
    @Provides
    public AppDatabase appDatabase(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    public FavoriteMoviesDao favoriteMoviesDao(AppDatabase appDatabase){
        return appDatabase.getFavoriteMoviesDao();
    }

}
