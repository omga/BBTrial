package com.andrewhatrus.brainbeantrialtask.screen.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrewhatrus.brainbeantrialtask.R;
import com.andrewhatrus.brainbeantrialtask.data.model.GitHubUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by Andrew on 19.08.2017.
 */
public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.ViewHolder> {

    private List<GitHubUser> users = new ArrayList<>();
    private Context context;
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();


    public SearchUsersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GitHubUser user = users.get(position);
        holder.textView.setText(user.getLogin());
        Glide.with(context)
                .load(user.getAvatarUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> onClickSubject.onNext(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setData(@NonNull List<GitHubUser> data) {
        users = new ArrayList<>(data.size());
        users.addAll(data);
        notifyDataSetChanged();
    }

    public List<GitHubUser> getData() {
        return users;
    }

    public void addData(@NonNull List<GitHubUser> data) {
        if(users!=null)
            users.addAll(data);
        else setData(data);
        notifyDataSetChanged();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    public PublishSubject<Integer> getPositionClicks(){
        return onClickSubject;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageProfile);
            textView = itemView.findViewById(R.id.textViewUsername);
        }
    }
}
