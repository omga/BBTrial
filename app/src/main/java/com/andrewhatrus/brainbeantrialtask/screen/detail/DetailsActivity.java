package com.andrewhatrus.brainbeantrialtask.screen.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrewhatrus.brainbeantrialtask.R;
import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUserFullProfile;
import com.andrewhatrus.brainbeantrialtask.data.model.UserRepo;
import com.andrewhatrus.brainbeantrialtask.screen.base.BaseLifecycleActivity;
import com.andrewhatrus.brainbeantrialtask.view.EmptyRecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends BaseLifecycleActivity {

    public static final String USER_EXTRA = "github_user";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textViewUsername)
    TextView userNameTextView;
    @BindView(R.id.textProfileDescription)
    TextView description;
    @BindView(R.id.textViewFullName)
    TextView fullName;
    @BindView(R.id.repos)
    TextView repos;
    @BindView(R.id.followers)
    TextView followers;
    @BindView(R.id.following)
    TextView following;
    @BindView(R.id.imageUserDetails)
    ImageView imageView;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String userName;
    GitHubUserFullProfile userFullProfile;
    private LinearLayoutManager layoutManager;
    DetailsViewModel viewModel;
    private ReposAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(getIntent()==null && getIntent().getParcelableExtra(USER_EXTRA)==null)
            return;
        userName = getIntent().getStringExtra(USER_EXTRA);
        userNameTextView.setText(userName);
        setupRecycler();
        viewModel = getViewModel();
        observeData();
    }

    private void observeData() {
        viewModel.isLoading().observe(this, aBoolean -> {
            if(aBoolean!=null && aBoolean)
                progressBar.setVisibility(View.VISIBLE);
            else progressBar.setVisibility(View.GONE);
        });

        MutableLiveData<List<UserRepo>> currentReposData = viewModel.getCurrentReposData();
        if(currentReposData==null || currentReposData.getValue()==null)
            loadRepos();
        else
            adapter.setData(currentReposData.getValue());

        MutableLiveData<GitHubUserFullProfile> currentUserData = viewModel.getCurrentUserData();
        if(currentUserData == null || currentUserData.getValue() == null)
            loadFullProfile();
        else {
            userFullProfile = currentUserData.getValue();
            fillViews();
        }
    }

    private DetailsViewModel getViewModel () {
        return ViewModelProviders.of(this).get(DetailsViewModel.class);
    }

    @MainThread
    private void loadRepos() {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.loadRepos(userName).observe(this, users -> {
            if(users!=null) {
                adapter.setData(users);
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    @MainThread
    private void loadFullProfile() {
        viewModel.loadFullProfile(userName).observe(this, user -> {
            userFullProfile = user;
            fillViews();
        });
    }

    private void fillViews() {
        description.setText(userFullProfile.getBio());
        fullName.setText(userFullProfile.getName());
        Glide.with(this)
                .load(userFullProfile.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
        followers.append(String.valueOf(userFullProfile.getFollowers()));
        following.append(String.valueOf(userFullProfile.getFollowing()));
        repos.append(String.valueOf(userFullProfile.getPublicRepos()));

    }

    private void setupRecycler() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReposAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new ReposScrollListener());

    }

    class ReposScrollListener extends RecyclerView.OnScrollListener {
        final int visibleThreshold = 5;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(viewModel==null ||
                    Objects.equals(viewModel.isLoading().getValue(), Boolean.TRUE) ||
                    adapter.getData().size() < 20)
                return;
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            if (adapter.getData().size() <= (lastVisibleItem + visibleThreshold)) {
                LiveData<List<UserRepo>> ld = viewModel.loadRepos(userName);
                ld.observe(DetailsActivity.this, repos -> {
                    adapter.addData(repos);
                });
            }
        }
    }
}
