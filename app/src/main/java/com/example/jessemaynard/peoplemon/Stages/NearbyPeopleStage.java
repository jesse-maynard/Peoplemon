package com.example.jessemaynard.peoplemon.Stages;

import android.app.Application;

import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.example.jessemaynard.peoplemon.R;
import com.example.jessemaynard.peoplemon.Riggers.SlideRigger;

/**
 * Created by jessemaynard on 11/11/16.
 */

public class NearbyPeopleStage extends IndexedStage {
    private final SlideRigger rigger;

    public NearbyPeopleStage(Application context) {
        super(PeopleMonMapStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public NearbyPeopleStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.nearby_users_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}