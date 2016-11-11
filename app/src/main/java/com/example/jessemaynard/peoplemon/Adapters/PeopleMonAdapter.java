package com.example.jessemaynard.peoplemon.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessemaynard.peoplemon.Models.User;
import com.example.jessemaynard.peoplemon.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PeopleMonAdapter extends RecyclerView.Adapter<PeopleMonAdapter.UserHolder> {

    public ArrayList<User> users;
    private Context context;

    public PeopleMonAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }


    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(context).inflate(R.layout.caught_peoplemon, parent, false);
        return new UserHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {

        if(position <users.size()){
            User user = users.get(position);
            holder.bindUser(user);
        }
    }

    @Override
    public int getItemCount() {
        return users.size()+1;
    }


    class UserHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.caught_avatar)
        ImageView caughtAvatar;

        @Bind(R.id.caught_username)
        TextView caughtUserName;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        // Adds the data to the view.
        public void bindUser(User user){
            caughtUserName.setText(user.getUsername());

            //Need to add in imageview and decoding process
//            caughtAvatar.setText
        }
    }
}