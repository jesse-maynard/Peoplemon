package com.example.jessemaynard.peoplemon.Network;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jessemaynard.peoplemon.Components.Constants;
import com.example.jessemaynard.peoplemon.PeopleMonApplication;

import java.util.Date;

/**
 * Created by jessemaynard on 11/7/16.
 */

public class UserStore {
    private static UserStore ourInstance = new UserStore();

    public static UserStore getInstance(){
        return ourInstance;
    }

    private SharedPreferences sharedPrefs = PeopleMonApplication.getInstance().getSharedPreferences("PeopleMonPrefs", Context.MODE_PRIVATE);

    public String getToken(){
        String theToken = sharedPrefs.getString(Constants.token, null);
        return theToken;}

    public void setToken(String token){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(Constants.token, token);
        editor.apply();
    }

    public Date getTokenExpiration(){
        Long expiration = sharedPrefs.getLong(Constants.tokenExpiration, 0);

        Date date = new Date(expiration);

        if (date.before(new Date())){
            return null;
        } else {
            return date;
        }
    }

    public void setTokenExpiration(Date expiration){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(Constants.tokenExpiration, expiration.getTime());
        editor.apply();
    }
}
