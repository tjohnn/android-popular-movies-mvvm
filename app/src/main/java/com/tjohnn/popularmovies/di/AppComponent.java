package com.tjohnn.popularmovies.di;

import android.app.Application;
import com.tjohnn.popularmovies.MoviesApp;
import com.tjohnn.popularmovies.data.repository.MoviesRepository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = { AppModule.class, ActivityBindingModule.class,
        NetworkModule.class, PicassoModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<MoviesApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
