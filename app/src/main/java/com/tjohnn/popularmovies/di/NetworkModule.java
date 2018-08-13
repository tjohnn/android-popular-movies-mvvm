package com.tjohnn.popularmovies.di;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.picasso.Picasso;
import com.tjohnn.popularmovies.BuildConfig;
import com.tjohnn.popularmovies.data.remote.ApiService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public abstract class NetworkModule {

    @Singleton
    @Provides
    static ApiService provideApiService(OkHttpClient okHttpClient, Gson gson){
        String BASE_URL = BuildConfig.API_BASE_URL;
        Retrofit.Builder retrofitBuilder;
        retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(BASE_URL);
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson));
        return retrofitBuilder.build().create(ApiService.class);
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkhttp(Interceptor interceptor, HttpLoggingInterceptor httpLoggingInterceptor, Cache cache){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .cache(cache)
        .addInterceptor(interceptor)
        .addInterceptor(httpLoggingInterceptor);
        return builder.build();
    }



    @Provides
    @Singleton
    static Cache provideCache(Context context){
        File cacheFile = new File(context.getCacheDir(), "HttpCache");
        cacheFile.mkdirs();

        return new Cache(cacheFile, 10 * 1000 * 1000); //10 MB
    }


    @Provides
    @Singleton
    static Interceptor provideInterceptor(){
        return chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    @Provides
    @Singleton
    static Gson provideGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        return  gsonBuilder.create();
    }

    @Provides
    @Singleton
    static HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.d(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


}
