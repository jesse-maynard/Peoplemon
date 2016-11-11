package com.example.jessemaynard.peoplemon.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jessemaynard on 11/9/16.
 */

public class User implements Comparable<User>{

    @SerializedName("UserId")
    private String id;

    @SerializedName("UserName")
    private String username;

    @SerializedName("AvatarBase64")
    private String avatar;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Created")
    private String created;

    @SerializedName("CaughtUserID")
    private String caughtUserId;

    @SerializedName("RadiusInMeters")
    private Float radiusInMeter;

    public User() {
    }

    public User(double latitude, double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public User(String UserId, Float radiusInMeter) {
        this.caughtUserId = UserId;
        this.radiusInMeter = radiusInMeter;
    }

    public User(String id, String username, String created, String avatar) {
        this.id = id;
        this.username = username;
        this.caughtUserId = created;
        this.avatar = avatar;
    }

    @Override
    public int compareTo(User user) {
        return 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCaughtUserId() {
        return caughtUserId;
    }

    public void setCaughtUserId(String caughtUserId) {
        this.caughtUserId = caughtUserId;
    }

    public double getRadiusInMeter() {
        return radiusInMeter;
    }

    public void setRadiusInMeter(Float radiusInMeter) {
        this.radiusInMeter = radiusInMeter;
    }
}
