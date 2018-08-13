package com.tjohnn.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.tjohnn.popularmovies.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;

@Singleton
public class PreferenceHelper {

    private static final String APP_PREF_NAME = "PopularMovies";
    private static final String SORTING_ORDER_PREF_KEY = "SortingOrderKey";

    private SharedPreferences preferences;

    @Inject
    PreferenceHelper(Context context) {
        preferences = context.getSharedPreferences(APP_PREF_NAME, MODE_PRIVATE);
    }

    public String getMovieSortingOrder(){
        return preferences.getString(SORTING_ORDER_PREF_KEY, Constants.BY_POPULARITY_VALUE);
    }

    public void updateSortingOrder(String value){
        preferences.edit().putString(SORTING_ORDER_PREF_KEY, value).apply();
    }

}
