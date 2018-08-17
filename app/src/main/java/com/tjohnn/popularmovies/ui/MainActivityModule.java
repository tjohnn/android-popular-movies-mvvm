package com.tjohnn.popularmovies.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.tjohnn.popularmovies.di.ActivityScoped;
import com.tjohnn.popularmovies.di.FragmentScoped;
import com.tjohnn.popularmovies.ui.moviedetail.MovieDetailFragment;
import com.tjohnn.popularmovies.ui.moviedetail.MovieDetailModule;
import com.tjohnn.popularmovies.ui.movies.MoviesFragment;
import com.tjohnn.popularmovies.ui.movies.MoviesFragmentModule;
import com.tjohnn.popularmovies.utils.navigator.BaseNavigator;
import com.tjohnn.popularmovies.utils.navigator.Navigator;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = MoviesFragmentModule.class)
    abstract MoviesFragment moviesFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = MovieDetailModule.class)
    abstract MovieDetailFragment movieDetailFragment();

    @Binds
    @ActivityScoped
    abstract AppCompatActivity mainActivity(MainActivity mainActivity);

    @Provides
    @ActivityScoped
    static BaseView baseView(MainActivity mainActivity){
        if(!(mainActivity instanceof BaseView)) throw  new RuntimeException("All Activity must implement BaseView");
        return (BaseView) mainActivity;
    }

    @Provides
    @ActivityScoped
    static BaseNavigator baseNavigator(AppCompatActivity activity){
        return new Navigator(activity);
    }

}
