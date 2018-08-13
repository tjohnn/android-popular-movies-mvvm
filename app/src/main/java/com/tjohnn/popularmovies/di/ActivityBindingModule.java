package com.tjohnn.popularmovies.di;

import com.tjohnn.popularmovies.ui.MainActivity;
import com.tjohnn.popularmovies.ui.MainActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();
}
