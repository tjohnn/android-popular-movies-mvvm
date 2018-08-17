package com.tjohnn.popularmovies;


import com.tjohnn.popularmovies.di.DaggerAppComponent;
import com.tjohnn.popularmovies.utils.TimberTree;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

/**
 * Created by Tjohn on 7/31/18.
 */

public class MoviesApp extends DaggerApplication {

    AndroidInjector<? extends DaggerApplication> injector;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return injector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        injector = DaggerAppComponent.builder().application(this).build();

        if (BuildConfig.DEBUG)
            Timber.plant(new TimberTree());
    }
}
