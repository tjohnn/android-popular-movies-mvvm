package com.tjohnn.popularmovies.utils.navigator;

import com.tjohnn.popularmovies.data.model.Movie;

public interface BaseNavigator {

    void finishActivity();

    void startActivity(Class cls, int requestCode);

    void openMovieDetailFragment(String movieId);

    void closeCurrentFragment();

    void returnToFirstFragment();

}
