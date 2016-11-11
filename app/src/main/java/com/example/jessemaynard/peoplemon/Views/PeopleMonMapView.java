package com.example.jessemaynard.peoplemon.Views;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessemaynard.peoplemon.Components.Utils;
import com.example.jessemaynard.peoplemon.MainActivity;
import com.example.jessemaynard.peoplemon.Models.Account;
import com.example.jessemaynard.peoplemon.Models.User;
import com.example.jessemaynard.peoplemon.Network.RestClient;
import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.example.jessemaynard.peoplemon.R;
import com.example.jessemaynard.peoplemon.Stages.AccountStage;
import com.example.jessemaynard.peoplemon.Stages.NearbyPeopleStage;
import com.example.jessemaynard.peoplemon.Stages.PeopleMonStage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jessemaynard on 11/7/16.
 */

public class PeopleMonMapView extends RelativeLayout implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener, LocationListener {

    final RestClient restClient = new RestClient();
    public static Location nuLocation;
    private Context context;

    private LocationRequest locationRequest;
    private LocationListener locationListener;
    public GoogleMap gMap;
    private GoogleApiClient googleApiClient;



// Variables for making calls to the API.
    private double currentLat;
    private double currentLong;
    private String name;
    private String id;
    private Integer radiusInMeters = 100;

    @Bind(R.id.map)
    MapView map;

    @Bind(R.id.peoplemon_list_button)
    FloatingActionButton peoplemonListButton;

    @Bind(R.id.radar_button)
    FloatingActionButton radarButton;

    @Bind(R.id.account_button)
    FloatingActionButton accountButton;


    public PeopleMonMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        map.onCreate(((MainActivity) getContext()).savedInstanceState);
        map.onResume();
        map.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        googleApiClient.connect();

        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000).setFastestInterval(2000);

        caught();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        }

    @Override
    public void onLocationChanged(Location location) {
        // Clear the map when the location is changed and handle a new location.
        gMap.clear();
        handleNewLocation(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add the map to a variable.
        gMap = googleMap;
        // Call the method to set up the map.
        setUpMap();
    }
    // Set up the map.
    public void setUpMap(){
        try {
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            gMap.setMyLocationEnabled(true);

        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            // Request the location when the app connects to the API.
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        } catch (SecurityException e){
            e.printStackTrace();
        }
        }

    private void handleNewLocation(final Location location){
        Log.d("-------------->", location.toString());
        // Obtain the current Location
        currentLat = location.getLatitude();
        currentLong = location.getLongitude();
        final LatLng latLng = new LatLng(currentLat, currentLong);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        // Set up the animation for the radar circle.
        final Circle circle = gMap.addCircle(new CircleOptions().center(latLng)
                .strokeColor(Color.MAGENTA).radius(500));

        ValueAnimator valAnim = new ValueAnimator();
        valAnim.setRepeatCount(ValueAnimator.INFINITE);
        valAnim.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
        valAnim.setIntValues(0, 100);
        valAnim.setDuration(1000);
        valAnim.setEvaluator(new IntEvaluator());
        valAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        valAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                circle.setRadius(animatedFraction * 100);
            }
        });

        nuLocation = location;

        // Call to the API to get logged in user info.
        restClient.getApiService().getInfo().enqueue(new Callback<Account>() {

            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                // Decode the account avatar.
                Bitmap userAvatar = Utils.decodeImage(account.getAvatar());
                // If the account does not have an avatar set use default icon.
                if (userAvatar != null) {
                    gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                            .fromBitmap(userAvatar)).snippet(id).title(account.getFullname())
                            .position(latLng));
                } else {
                    gMap.addMarker(new MarkerOptions().snippet(id).title(account.getFullname()).position(latLng));
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });

        // Call to the API to determine the users nearby.
        restClient.getApiService().nearby(500).enqueue(new Callback<User[]>() {

            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                if (response.isSuccessful()) {
                    Log.d("More", response.body().length + "Something");

                    for (User user : response.body()) {
                        id = user.getId();

                        Location nearLocation = new Location("");
                        currentLat = user.getLatitude();
                        currentLong = user.getLongitude();

                        nearLocation.setLatitude(currentLat);
                        nearLocation.setLongitude(currentLong);

                        LatLng latLng = new LatLng(currentLat, currentLong);

                        user.setRadiusInMeter(location.distanceTo(nearLocation));
                        if (location.distanceTo(nearLocation) <= 500) {
                            Bitmap image = Utils.decodeImage(user.getAvatar());
                            if (image != null) {
                                gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                                        .fromBitmap(image)).snippet(id).title(user.getUsername())
                                        .position(latLng));
                            } else {
                                gMap.addMarker(new MarkerOptions().snippet(id).title(user.getUsername()).position(latLng));
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
            }
        });
        // Listener to tell when a user is clicked.
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                name = marker.getTitle();
                id = marker.getSnippet();

                catchUser();
                return false;
            }
        });
        // Start the animation for the circle.
        valAnim.start();

        // Call the check-in method.
        checkIn();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @OnClick(R.id.account_button)
    public void accountTapped(){
        Flow flow = PeopleMonApplication.getMainFlow();

        History newHistory = flow.getHistory().buildUpon().push(new AccountStage()).build();

        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.peoplemon_list_button)
    public void peoplemonTapped(){
        Flow flow = PeopleMonApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon().push(new PeopleMonStage()).build();

        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.radar_button)
    public void radarTapped(){
        //TODO, Link this to the radar modal.
        Flow flow = PeopleMonApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon().push(new NearbyPeopleStage()).build();

        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    public void checkIn(){
        User user = new User(currentLat, currentLong);

        restClient.getApiService().checkIn(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){

                } else {
                    Toast.makeText(context, "Check in Failed" + ": " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Check-In Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void catchUser() {
        restClient.getApiService().catchUser(id, radiusInMeters).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("---------------", name + ": " + id);
                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                    caught();
                } else {
                    Toast.makeText(context, name + " Is Too Far Away", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Can't Catch User : Too Far Away", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void caught(){

        restClient.getApiService().caught().enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                if (response.isSuccessful()){
                    for (User user : response.body()){
                        Log.d("<><<><><><><<", user.getUsername());
                    }
                } else {
                    Toast.makeText(context, "Something Went Wrong" + ": " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {

            }
        });
    }

}
