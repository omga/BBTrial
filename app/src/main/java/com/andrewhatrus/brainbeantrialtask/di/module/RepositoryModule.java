package com.andrewhatrus.brainbeantrialtask.di.module;

import android.support.annotation.NonNull;

import com.andrewhatrus.brainbeantrialtask.data.Repository;
import com.andrewhatrus.brainbeantrialtask.data.service.GitHubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 18.08.2017.
 */

@Module
public class RepositoryModule {

    @Provides
    @NonNull
    @Singleton
    Repository provideRepository(GitHubService gitHubService) {
        return new Repository(gitHubService);
    }
}