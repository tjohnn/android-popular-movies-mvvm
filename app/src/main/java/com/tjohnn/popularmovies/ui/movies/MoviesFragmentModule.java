package com.tjohnn.popularmovies.ui.movies;

import android.support.v7.app.AppCompatActivity;

import com.tjohnn.popularmovies.data.repository.MoviesRepository;
import com.tjohnn.popularmovies.di.FragmentScoped;
import com.tjohnn.popularmovies.ui.MainActivity;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public abstract class MoviesFragmentModule {

    @Provides
    @FragmentScoped
    static MoviesViewModel moviesViewModel(MoviesRepository moviesRepository, BaseNavigator baseNavigator){
        return new MoviesViewModel(moviesRepository, baseNavigator);
    }

    @Provides
    @FragmentScoped
    static CompositeDisposable compositeDisposable(){
        return new CompositeDisposable();
    }

}
