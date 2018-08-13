package com.tjohnn.popularmovies.utils;

import timber.log.Timber;

/**
 * Created by Tjohn on 6/28/18.
 */


public class TimberTree extends Timber.DebugTree {

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return String.format("C:%s:%s",
                super.createStackElementTag(element),
                element.getLineNumber());
    }

}
