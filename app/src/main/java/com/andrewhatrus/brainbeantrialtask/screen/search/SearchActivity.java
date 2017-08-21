package com.andrewhatrus.brainbeantrialtask.screen.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andrewhatrus.brainbeantrialtask.R;
import com.andrewhatrus.brainbeantrialtask.Util;
import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUser;
import com.andrewhatrus.brainbeantrialtask.screen.base.BaseLifecycleActivity;
import com.andrewhatrus.brainbeantrialtask.screen.detail.DetailsActivity;
import com.andrewhatrus.brainbeantrialtask.view.EmptyRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseLifecycleActivity {

    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.emptyView)
    View emptyView;
    SearchViewModel viewModel;
    private SearchUsersAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        checkConnection();
        viewModel = getViewModel();
        ButterKnife.bind(this);
        setupRecycler();
        setupSearchView();
        observeData();
    }

    private SearchViewModel getViewModel () {
        return ViewModelProviders.of(this).get(SearchViewModel.class);
    }

    private void observeData() {
        viewModel.getCurrentData()
                .observe(this, users -> {
                    if(users!=null)
                        adapter.setData(users);
                } );
        viewModel.isLoading().observe(this, aBoolean -> {
            if(aBoolean!=null && aBoolean)
                progressBar.setVisibility(View.VISIBLE);
            else progressBar.setVisibility(View.GONE);
        });
    }

    private void setupSearchView() {
        searchView.setOnCloseListener( () -> {
            adapter.clear();
            return false;
        });
        RxSearchView.queryTextChanges(searchView)
                .observeOn(Schedulers.computation())
                .debounce(500, TimeUnit.MILLISECONDS)
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence ->  {
                    if(charSequence.length() == 0)
                        adapter.clear();
                    else {
                        viewModel.setPage(0);
                        doSearch(String.valueOf(charSequence));
                    }
                });

    }

    @MainThread
    private void doSearch(String s) {
        viewModel.searchUsers(s).observe(this, users -> {
            if(users!=null) {
                adapter.setData(users);
            }
        });
    }

    private void checkConnection() {
        if(!Util.isNetworkAvailable(this)) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupRecycler() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchUsersAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(emptyView);

        adapter.getPositionClicks().subscribe(position -> {
            Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.USER_EXTRA, adapter.getData().get(position).getLogin());
            startActivity(intent);
        });
        recyclerView.addOnScrollListener(new UsersScrollListener());
    }

    class UsersScrollListener extends RecyclerView.OnScrollListener {
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
                LiveData<List<GitHubUser>> ld = viewModel.searchUsers(String.valueOf(searchView.getQuery()));
                ld.observe(SearchActivity.this, users -> {
                    Log.d("SCROLL", "size: " + users.size());
                    adapter.addData(users);
                });
            }
        }
    }
}
