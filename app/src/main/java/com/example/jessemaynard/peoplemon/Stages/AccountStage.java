package com.example.jessemaynard.peoplemon.Stages;

import android.app.Application;
import android.support.design.widget.TextInputEditText;

import com.davidstemmer.screenplay.stage.Stage;
import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.example.jessemaynard.peoplemon.R;
import com.example.jessemaynard.peoplemon.Riggers.SlideRigger;

/**
 * Created by jessemaynard on 11/9/16.
 */

public class AccountStage extends IndexedStage {

    private final SlideRigger rigger;

    public AccountStage(Application context){
        super(LoginStage.class.getName());
        this.rigger= new SlideRigger(context);
    }

    public AccountStage(){
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.account_view;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }

}
