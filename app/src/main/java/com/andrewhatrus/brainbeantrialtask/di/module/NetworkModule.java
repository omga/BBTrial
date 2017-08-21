package com.andrewhatrus.brainbeantrialtask.di.module;

import android.support.annotation.NonNull;

import com.andrewhatrus.brainbeantrialtask.data.service.GitHubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andrew on 18.08.2017.
 */
@Module
public class NetworkModule {

    private Retrofit retrofit;

    public NetworkModule () {
        retrofit = buildRetrofit();
    }

    @Singleton
    @Provides
    @NonNull
    GitHubService provideGitHubService() {
        return retrofit.create(GitHubService.class);
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(GitHubService.BASE_URL)
                .client(buildOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @NonNull
    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }
}
