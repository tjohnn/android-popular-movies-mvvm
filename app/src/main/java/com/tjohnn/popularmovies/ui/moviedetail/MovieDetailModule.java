package com.tjohnn.popularmovies.ui.moviedetail;

import com.tjohnn.popularmovies.data.repository.MoviesRepository;
import com.tjohnn.popularmovies.di.FragmentScoped;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public abstract class MovieDetailModule {

    @Provides
    @FragmentScoped
    static MovieDetailViewModel moviesViewModel(String movieId, MoviesRepository moviesRepository, BaseNavigator baseNavigator){
        return new MovieDetailViewModel(movieId, moviesRepository, baseNavigator);
    }

    @Provides
    @FragmentScoped
    static CompositeDisposable compositeDisposable(){
        return new CompositeDisposable();
    }

    @Provides
    @FragmentScoped
    static  String movieId(MovieDetailFragment movieDetailFragment){
        if (movieDetailFragment.getArguments() != null) {
            return movieDetailFragment.getArguments().getString(MovieDetailFragment.MOVIE_ID_KEY);
        }
        return null;
    }
}
