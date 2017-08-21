package com.andrewhatrus.brainbeantrialtask.data;

import android.support.annotation.NonNull;

import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUser;
import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUserFullProfile;
import com.andrewhatrus.brainbeantrialtask.data.model.SearchResult;
import com.andrewhatrus.brainbeantrialtask.data.model.UserRepo;
import com.andrewhatrus.brainbeantrialtask.data.service.GitHubService;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Andrew on 17.08.2017.
 */
public class Repository {

    GitHubService gitHubService;

    public Repository(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @NonNull
    public Observable<List<GitHubUser>> searchUsers(String query, int page) {
        return gitHubService
                .searchUsers(query, page)
                .map(SearchResult::getGitHubUsers);
    }

    @NonNull
    public Observable<List<GitHubUser>> searchUsers(String query) {
        return searchUsers(query, 1);
    }

    @NonNull
    public Observable<GitHubUserFullProfile> getFullProfile(String username) {
        return gitHubService
                .getUser(username);
    }

    @NonNull
    public Observable<List<UserRepo>> getUserRepos(String userName, int page) {
        return gitHubService
                .getUserRepos(userName,page);
    }

    @NonNull
    public Observable<List<UserRepo>> getUserRepos(String userName) {
        return getUserRepos(userName, 0);
    }


}
