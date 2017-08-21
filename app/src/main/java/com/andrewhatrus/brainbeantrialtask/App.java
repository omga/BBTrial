package com.andrewhatrus.brainbeantrialtask;

import android.app.Application;
import android.content.Context;

import com.andrewhatrus.brainbeantrialtask.di.AppComponent;
import com.andrewhatrus.brainbeantrialtask.di.DaggerAppComponent;
import com.andrewhatrus.brainbeantrialtask.di.module.RepositoryModule;

/**
 * Created by Andrew on 18.08.2017.
 */
public class App extends Application {

    private static AppComponent component;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static AppComponent getComponent() {
        return component;
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .repositoryModule(new RepositoryModule())
                .build();
    }
}
