package com.example.jessemaynard.peoplemon.Views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jessemaynard.peoplemon.Adapters.PeopleNearbyAdapter;
import com.example.jessemaynard.peoplemon.Models.User;
import com.example.jessemaynard.peoplemon.Network.RestClient;
import com.example.jessemaynard.peoplemon.R;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jessemaynard on 11/11/16.
 */

public class NearbyPeopleView extends LinearLayout  {


        private Context context;
        private PeopleNearbyAdapter nearbyAdapter;

        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;


        public NearbyPeopleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            ButterKnife.bind(this);

            nearbyAdapter = new PeopleNearbyAdapter(new ArrayList<User>(), context);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(nearbyAdapter);

            listNearbyPeople();
        }

        private void listNearbyPeople(){

            RestClient restClient = new RestClient();
            restClient.getApiService().nearby(500).enqueue(new Callback<User[]>() {

                @Override
                public void onResponse(Call<User[]> call, Response<User[]> response) {
                    if (response.isSuccessful()){
                        nearbyAdapter.users = new ArrayList<User>(Arrays.asList(response.body()));

                        for (User user : nearbyAdapter.users){
                            Log.d(user.getUsername(),"IS" + user.getRadiusInMeter());
                            nearbyAdapter.notifyDataSetChanged();
                        }

                    }else{
                        Toast.makeText(context,"Get User Info Failed" + ": " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User[]> call, Throwable t) {
                    Toast.makeText(context,"Get User Info Failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


