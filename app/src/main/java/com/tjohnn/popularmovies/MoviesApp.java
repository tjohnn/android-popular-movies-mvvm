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

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG)
            Timber.plant(new TimberTree());
    }
}
