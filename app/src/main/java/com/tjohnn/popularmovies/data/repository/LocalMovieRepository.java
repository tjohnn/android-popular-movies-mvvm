package com.tjohnn.popularmovies.data.repository;

import android.arch.lifecycle.LiveData;

import com.tjohnn.popularmovies.data.local.FavoriteMovie;
import com.tjohnn.popularmovies.data.local.FavoriteMoviesDao;
import com.tjohnn.popularmovies.data.dtos.Movie;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import timber.log.Timber;

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

    public LiveData<List<Movie>> getFavoriteMovies(){
        return mFavoriteMoviesDao.getFavoriteMovies();
    }

    public Completable createFavoriteMovie(FavoriteMovie favoriteMovie){
        Timber.d("Fav movie created");
        return Completable.fromAction(() -> mFavoriteMoviesDao.insertFavoriteMovie(favoriteMovie));
    }
    public Completable updateFavoriteMovie(FavoriteMovie favoriteMovie){
        return Completable.fromAction(() -> mFavoriteMoviesDao.updateFavoriteMovie(favoriteMovie));
    }
    public Completable deleteFavoriteMovie(FavoriteMovie favoriteMovie){
        Timber.d("Fav movie deleted");
        return Completable.fromAction(() -> mFavoriteMoviesDao.deleteFavoriteMovie(favoriteMovie));
    }
}
