package com.andrewhatrus.brainbeantrialtask.data.service;


import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUserFullProfile;
import com.andrewhatrus.brainbeantrialtask.data.model.SearchResult;
import com.andrewhatrus.brainbeantrialtask.data.model.UserRepo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andrew on 17.08.2017.
 */
public interface GitHubService {

    String BASE_URL = "https://api.github.com/";
    String SEARCH_USERS = "search/users";
    String GET_USER = "/users/{username}";
    String GET_USER_REPOS = "/users/{username}/repos";

    @GET(SEARCH_USERS)
    Observable<SearchResult> searchUsers(@Query("q") String query, @Query("page") int page);

    @GET(GET_USER)
    Observable<GitHubUserFullProfile> getUser(@Path("username") String username);

    @GET(GET_USER_REPOS)
    Observable<List<UserRepo>> getUserRepos(@Path("username") String username, @Query("page") int page);

}
