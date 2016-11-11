package com.example.jessemaynard.peoplemon.Views;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jessemaynard.peoplemon.Adapters.PeopleNearbyAdapter;
import com.example.jessemaynard.peoplemon.Models.User;
import com.example.jessemaynard.peoplemon.Network.RestClient;
import com.example.jessemaynard.peoplemon.R;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.jessemaynard.peoplemon.Views.PeopleMonMapView.nuLocation;


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
                    if (response.isSuccessful()) {
                        nearbyAdapter.users = new ArrayList<User>(Arrays.asList(response.body()));

                        for (User user : nearbyAdapter.users) {
                            Location nearbyUserLocation = new Location("");
                            nearbyUserLocation.setLatitude(user.getLatitude());
                            nearbyUserLocation.setLongitude(user.getLongitude());
                            user.setRadiusInMeter(nuLocation.distanceTo(nearbyUserLocation));
                        }
                        Collections.sort(nearbyAdapter.users);
                        nearbyAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<User[]> call, Throwable t) {
                    Toast.makeText(context,"Get User Info Failed", Toast.LENGTH_LONG).show();
                }
            });
        }


    }


