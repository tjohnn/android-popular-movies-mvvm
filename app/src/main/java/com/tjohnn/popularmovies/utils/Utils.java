package com.tjohnn.popularmovies.utils;

import android.net.Uri;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import timber.log.Timber;

public class Utils {



    public static String processNetworkError(Throwable throwable){
        String message;
        if(throwable instanceof HttpException){
            message = "Server error!! Please try later.";
        }
        else if (throwable instanceof SocketTimeoutException) {
            message = "Network timeout! Ensure a better connection and retry.";
        }
        else if (throwable instanceof IOException) {
            message = "Network error. Ensure you are connected to internet";
        }
        else{
            message = throwable.getMessage();
        }
        return message;
    }

    public static Uri getYoutubeVideoUri(String videoId){
        return Uri.parse("http://www.youtube.com/watch?v=" + videoId);
    }

}
