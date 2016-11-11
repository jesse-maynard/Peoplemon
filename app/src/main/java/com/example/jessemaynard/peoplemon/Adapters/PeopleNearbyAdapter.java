package com.example.jessemaynard.peoplemon.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jessemaynard.peoplemon.Models.User;
import com.example.jessemaynard.peoplemon.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jessemaynard on 11/9/16.
 */

public class PeopleNearbyAdapter extends RecyclerView.Adapter<PeopleNearbyAdapter.UserHolder> {

    public ArrayList<User> users;
    private Context context;

    public PeopleNearbyAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }


    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(context).inflate(R.layout.nearby_user, parent, false);
        return new UserHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {

        if (position < users.size()) {
            User user = users.get(position);
            holder.bindUser(user);
        }
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }


    class UserHolder extends RecyclerView.ViewHolder {

//        @Bind(R.id.nearby_avatar)
//        ImageView nearbyAvatar;

        @Bind(R.id.nearby_username)
        TextView nearbyUsername;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // Adds the data to the view.
        public void bindUser(User user) {
            // Set the username of the nearby people.
            nearbyUsername.setText(user.getUsername());
        }
    }
}