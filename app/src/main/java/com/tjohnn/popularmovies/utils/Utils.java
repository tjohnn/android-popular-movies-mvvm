package com.tjohnn.popularmovies.utils;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.net.SocketTimeoutException;

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


}
