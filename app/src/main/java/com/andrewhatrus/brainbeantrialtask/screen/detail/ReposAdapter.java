package com.andrewhatrus.brainbeantrialtask.screen.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewhatrus.brainbeantrialtask.R;
import com.andrewhatrus.brainbeantrialtask.data.model.UserRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 20.08.2017.
 */
public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    private List<UserRepo> repos = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_repo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReposAdapter.ViewHolder holder, int position) {
        UserRepo user = repos.get(position);
        holder.name.setText(user.getName());
        holder.description.setText(user.getDescription());
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public void setData(@NonNull List<UserRepo> data) {
        repos = new ArrayList<>(data.size());
        repos.addAll(data);
        notifyDataSetChanged();
    }

    public List<UserRepo> getData() {
        return repos;
    }

    public void addData(@NonNull List<UserRepo> data) {
        if(repos !=null)
            repos.addAll(data);
        else setData(data);
        notifyDataSetChanged();
    }

    public void clear() {
        repos.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textRepoName);
            description = itemView.findViewById(R.id.textRepoDescription);
        }
    }
}