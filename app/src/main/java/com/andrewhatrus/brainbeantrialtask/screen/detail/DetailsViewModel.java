package com.andrewhatrus.brainbeantrialtask.screen.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.andrewhatrus.brainbeantrialtask.App;
import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUserFullProfile;
import com.andrewhatrus.brainbeantrialtask.data.model.UserRepo;
import com.andrewhatrus.brainbeantrialtask.screen.base.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andrew on 20.08.2017.
 */
public class DetailsViewModel extends BaseViewModel {

    @Nullable
    private MutableLiveData<List<UserRepo>> usersLiveData;

    @Nullable
    private MutableLiveData<GitHubUserFullProfile> fullProfileLiveData;

    private int page;

    @Nullable
    MutableLiveData<List<UserRepo>> getCurrentReposData() {
        return usersLiveData;
    }

    @Nullable
    MutableLiveData<GitHubUserFullProfile> getCurrentUserData() {
        return fullProfileLiveData;
    }

    @MainThread
    LiveData<List<UserRepo>> loadRepos(String userName) {
        isLoading.setValue(true);
        usersLiveData = new MutableLiveData<>();
        repository.getUserRepos(userName, ++page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    usersLiveData.setValue(list);
                    isLoading.setValue(false);
                }, throwable -> Toast.makeText(App.getAppContext(),
                        "error: " +throwable.getMessage(),
                        Toast.LENGTH_LONG).show());
        return usersLiveData;
    }

    @MainThread
    LiveData<GitHubUserFullProfile> loadFullProfile(String userName) {
        isLoading.setValue(true);
        fullProfileLiveData = new MutableLiveData<>();
        repository.getFullProfile(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fullProfile -> {
                    fullProfileLiveData.setValue(fullProfile);
                    isLoading.setValue(false);
                }, throwable -> Toast.makeText(App.getAppContext(),
                        "error: " +throwable.getMessage(),
                        Toast.LENGTH_LONG).show());
        return fullProfileLiveData;
    }

    int getPage() {
        return page;
    }

    void setPage(int page) {
        this.page = page;
    }
}