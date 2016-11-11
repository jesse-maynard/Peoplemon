package com.example.jessemaynard.peoplemon.Stages;

import android.app.Application;

import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.example.jessemaynard.peoplemon.R;
import com.example.jessemaynard.peoplemon.Riggers.SlideRigger;

/**
 * Created by jessemaynard on 11/10/16.
 */

public class PeopleMonStage extends IndexedStage {
    private final SlideRigger rigger;

    public PeopleMonStage(Application context) {
        super(PeopleMonStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public PeopleMonStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.caught_peoplemon_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
