package com.tjohnn.popularmovies.di;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PicassoModule {

    @Singleton
    @Provides
    static Picasso providePicasso(Context context, OkHttp3Downloader okHttp3Downloader){
        return new Picasso.Builder(context).downloader(okHttp3Downloader).build();
    }

    @Provides
    @Singleton
    public OkHttp3Downloader okHttp3Downloader(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }


}
