package com.tjohnn.popularmovies.data.repository;

import android.arch.lifecycle.LiveData;

import com.tjohnn.popularmovies.data.local.FavoriteMovie;
import com.tjohnn.popularmovies.data.local.FavoriteMoviesDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;

@Singleton
public class LocalMovieRepository {

    private FavoriteMoviesDao mFavoriteMoviesDao;

    @Inject
    public LocalMovieRepository(FavoriteMoviesDao favoriteMoviesDao){

        mFavoriteMoviesDao = favoriteMoviesDao;
    }

    public LiveData<FavoriteMovie> getFavoriteMovieById(long id){
        return mFavoriteMoviesDao.getFavoriteMovieById(id);
    }

    public LiveData<List<FavoriteMovie>> getFavoriteMovie(){
        return mFavoriteMoviesDao.getFavoriteMovies();
    }

    public Completable createFavoriteMovie(FavoriteMovie favoriteMovie){
        return Completable.fromAction(() -> mFavoriteMoviesDao.insertFavoriteMovie(favoriteMovie));
    }
    public Completable updateFavoriteMovie(FavoriteMovie favoriteMovie){
        return Completable.fromAction(() -> mFavoriteMoviesDao.updateFavoriteMovie(favoriteMovie));
    }
    public Completable deleteFavoriteMovie(FavoriteMovie favoriteMovie){
        return Completable.fromAction(() -> mFavoriteMoviesDao.deleteFavoriteMovie(favoriteMovie));
    }
}
