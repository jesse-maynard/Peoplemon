package com.example.jessemaynard.peoplemon;

import android.app.Application;

import com.example.jessemaynard.peoplemon.Stages.LoginStage;
import com.example.jessemaynard.peoplemon.Stages.PeopleMonMapStage;

import flow.Flow;
import flow.History;

/**
 * Created by jessemaynard on 11/7/16.
 */

public class PeopleMonApplication extends Application {
    private static PeopleMonApplication application;
    public final Flow mainFlow = new Flow(History.single(new PeopleMonMapStage()));

    public static final String API_BASE_URL = "https://efa-peoplemon-api.azurewebsites.net:443/";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static PeopleMonApplication getInstance(){
        return application;
    }

    public static Flow getMainFlow(){
        return getInstance().mainFlow;
    }
}
