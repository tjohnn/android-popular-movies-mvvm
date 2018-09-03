package com.tjohnn.popularmovies.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import com.tjohnn.popularmovies.R;
import com.tjohnn.popularmovies.ui.movies.MoviesFragment;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements BaseView{

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main,
                    MoviesFragment.newInstance(),
                    MoviesFragment.TAG).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getSupportFragmentManager().popBackStackImmediate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayUpNavigation(boolean display){
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(display);
            actionBar.setDisplayHomeAsUpEnabled(display);
            actionBar.setDisplayShowHomeEnabled(display);
        }
    }
}
