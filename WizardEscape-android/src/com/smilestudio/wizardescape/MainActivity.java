package com.smilestudio.wizardescape;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.umeng.analytics.game.UMGameAgent;

public class MainActivity extends AndroidApplication implements AnalyticsListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init Umeng
        UMGameAgent.setDebugMode(true);
        UMGameAgent.init( this );

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        WizardEscape escape = new WizardEscape();
        escape.setAnalyticsListener(this);
        initialize(escape, cfg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMGameAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMGameAgent.onPause(this);
    }

    @Override
    public void startMission(String mission) {
        System.out.println("============ startMission : " + mission);
        UMGameAgent.startLevel(mission);
    }

    @Override
    public void failMission(String mission) {
        System.out.println("============ failMission : " + mission);
        UMGameAgent.failLevel(mission);
    }

    @Override
    public void finishMission(String mission) {
        System.out.println("============ finishMission : " + mission);
        UMGameAgent.finishLevel(mission);
    }

    @Override
    public void use(String mission, int steps) {
        System.out.println("============ use : " + mission + "," + steps);
        UMGameAgent.use(mission, steps, 0);
    }

}