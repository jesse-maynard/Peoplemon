package com.example.jessemaynard.peoplemon.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jessemaynard.peoplemon.Components.Utils;
import com.example.jessemaynard.peoplemon.Models.User;
import com.example.jessemaynard.peoplemon.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

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

        final User near = users.get(position);
        holder.bindUser(near);

    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }


    class UserHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.nearby_avatar)
        ImageView nearbyAvatar;

        @Bind(R.id.nearby_username)
        TextView nearbyUsername;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // Adds the data to the view.
        public void bindUser(User user) {
            // Set the username of the nearby people.
            DecimalFormat decfor = new DecimalFormat("0.00");

            nearbyUsername.setText(user.getUsername());

            Bitmap userAvatar = Utils.decodeImage(user.getAvatar());
            if (userAvatar == null){
                nearbyAvatar.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
            } else {
                nearbyAvatar.setImageBitmap(userAvatar);
            }

            Collections.sort(users);
        }
    }
}