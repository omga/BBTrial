package com.andrewhatrus.brainbeantrialtask.screen.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.andrewhatrus.brainbeantrialtask.App;
import com.andrewhatrus.brainbeantrialtask.data.Repository;

import javax.inject.Inject;

/**
 * Created by Andrew on 19.08.2017.
 */
public class BaseViewModel extends ViewModel {

    @NonNull
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @Inject
    @NonNull
    protected Repository repository;

    public BaseViewModel() {
        App.getComponent().inject(this);
    }

    @NonNull
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

}
