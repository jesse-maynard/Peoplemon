package com.example.jessemaynard.peoplemon.Stages;

import android.app.Application;

import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.example.jessemaynard.peoplemon.R;
import com.example.jessemaynard.peoplemon.Riggers.SlideRigger;

/**
 * Created by jessemaynard on 11/7/16.
 */
//TODO Add map view and the button functions to this.
public class PeopleMonMapStage extends IndexedStage {
    private final SlideRigger rigger;

    public PeopleMonMapStage(Application context) {
        super(PeopleMonMapStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public PeopleMonMapStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.peoplemon_map_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
