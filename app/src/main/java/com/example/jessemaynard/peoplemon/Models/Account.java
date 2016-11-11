package com.example.jessemaynard.peoplemon.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import static com.example.jessemaynard.peoplemon.Components.Constants.grantType;

/**
 * Created by jessemaynard on 11/7/16.
 */

public class Account {


    @SerializedName("Email")
    private String email;

    @SerializedName("FullName")
    private String fullname;

    @SerializedName("password")
    private String password;

    @SerializedName("AvatarBase64")
    private String avatar;

    @SerializedName("access_token")
    private String token;

    @SerializedName(".expires")
    private Date tokenExpiration;

    @SerializedName("grantType")
    private String grant = grantType;

    public Account() {
    }

    public Account(String email, String password, String grant) {
        this.email = email;
        this.password = password;
        this.grant = grant;
    }

    public Account(String fullname, String avatar) {
        this.fullname = fullname;
        this.avatar = avatar;
    }

    public Account(String email, String fullname, String password, String avatar) {
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Date tokeExpiration) {
        this.tokenExpiration = tokeExpiration;
    }
}
