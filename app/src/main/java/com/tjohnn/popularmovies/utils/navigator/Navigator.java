package com.tjohnn.popularmovies.utils.navigator;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tjohnn.popularmovies.R;
import com.tjohnn.popularmovies.ui.moviedetail.MovieDetailFragment;

public class Navigator implements BaseNavigator {


    private AppCompatActivity mActivity;

    public Navigator(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void startActivity(Class cls, int requestCode) {

    }

    @Override
    public void openMovieDetailFragment(String movieId) {
        MovieDetailFragment fragment = MovieDetailFragment.newInstance(movieId);
        openFragment(mActivity.getSupportFragmentManager(), fragment, MovieDetailFragment.TAG, R.id.content_main);
    }

    @Override
    public void closeCurrentFragment() {
        mActivity.getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void returnToFirstFragment() {
        mActivity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    public static void openFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, String tag, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

}
