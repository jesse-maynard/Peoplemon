package com.example.jessemaynard.peoplemon.Stages;

import android.app.Application;

import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.example.jessemaynard.peoplemon.R;
import com.example.jessemaynard.peoplemon.Riggers.SlideRigger;

/**
 * Created by jessemaynard on 11/7/16.
 */


public class LoginStage extends IndexedStage {
    private final SlideRigger rigger;

    public LoginStage(Application context){
        super(LoginStage.class.getName());
        this.rigger= new SlideRigger(context);
    }

    public LoginStage(){
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}
