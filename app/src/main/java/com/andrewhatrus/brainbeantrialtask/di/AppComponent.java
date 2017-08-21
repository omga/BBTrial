package com.andrewhatrus.brainbeantrialtask.di;

import com.andrewhatrus.brainbeantrialtask.di.module.NetworkModule;
import com.andrewhatrus.brainbeantrialtask.di.module.RepositoryModule;
import com.andrewhatrus.brainbeantrialtask.screen.base.BaseViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Andrew on 18.08.2017.
 */
@Component(modules = {RepositoryModule.class, NetworkModule.class})
@Singleton
public interface AppComponent {

    void inject(BaseViewModel activity);

}
