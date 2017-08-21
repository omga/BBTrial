package com.andrewhatrus.brainbeantrialtask.screen.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.andrewhatrus.brainbeantrialtask.App;
import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUser;
import com.andrewhatrus.brainbeantrialtask.screen.base.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andrew on 19.08.2017.
 */
public class SearchViewModel extends BaseViewModel {

    @Nullable
    private MutableLiveData<List<GitHubUser>> usersLiveData;
    private int page;

    MutableLiveData<List<GitHubUser>> getCurrentData() {
        if(usersLiveData == null)
            usersLiveData = new MutableLiveData<>();
        return usersLiveData;
    }

    @MainThread
    LiveData<List<GitHubUser>> searchUsers(String query) {
        isLoading.setValue(true);
        usersLiveData = new MutableLiveData<>();
        repository.searchUsers(query, ++page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    usersLiveData.setValue(list);
                    isLoading.setValue(false);
                }, throwable -> {
                    Toast.makeText(App.getAppContext(),
                            "error: " + throwable.getMessage(),
                            Toast.LENGTH_LONG).show();
                    isLoading.setValue(false);
                });
        return usersLiveData;
    }

    int getPage() {
        return page;
    }

    void setPage(int page) {
        this.page = page;
    }
}
