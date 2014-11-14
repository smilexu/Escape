package com.smilestudio.wizardescape;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bodong.dianjinweb.DianJinPlatform;
import com.umeng.analytics.game.UMGameAgent;

public class MainActivity extends AndroidApplication implements AnalyticsListener, AdListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init Umeng
        UMGameAgent.setDebugMode(true);
        UMGameAgent.init( this );

        //init ad
        DianJinPlatform.initialize(this, 61609, "8839a8811fca305d9a9e281493f8d042", 1001);
        DianJinPlatform.hideFloatView(this);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        WizardEscape escape = new WizardEscape();
        escape.setAnalyticsListener(this);
        escape.setAdListener(this);
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
    protected void onDestroy() {
        super.onDestroy();

        DianJinPlatform.destory(this);
    }

    @Override
    public void startMission(String mission) {
        UMGameAgent.startLevel(mission);
    }

    @Override
    public void failMission(String mission) {
        UMGameAgent.failLevel(mission);
    }

    @Override
    public void finishMission(String mission) {
        UMGameAgent.finishLevel(mission);
    }

    @Override
    public void use(String mission, int steps) {
        UMGameAgent.use(mission, steps, 0);
    }

    @Override
    public void showAdWall() {
        DianJinPlatform.showOfferWall(this);
    }

}