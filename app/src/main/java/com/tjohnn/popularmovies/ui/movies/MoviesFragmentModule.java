package com.tjohnn.popularmovies.ui.movies;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import com.tjohnn.popularmovies.data.repository.LocalMovieRepository;
import com.tjohnn.popularmovies.data.repository.MoviesRepository;
import com.tjohnn.popularmovies.di.FragmentScoped;
import com.tjohnn.popularmovies.ui.moviedetail.MovieDetailViewModel;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public abstract class MoviesFragmentModule {

    @Provides
    @FragmentScoped
    static MoviesViewModel moviesViewModel(Context application, MoviesFragment moviesFragment, MoviesRepository moviesRepository, BaseNavigator baseNavigator, LocalMovieRepository localMovieRepository){
        return ViewModelProviders.of(moviesFragment,
                new MoviesViewModel.ViewModelFactory((Application) application, moviesRepository,
                        localMovieRepository, baseNavigator)).get(MoviesViewModel.class);
    }

    @Provides
    @FragmentScoped
    static CompositeDisposable compositeDisposable(){
        return new CompositeDisposable();
    }

}
