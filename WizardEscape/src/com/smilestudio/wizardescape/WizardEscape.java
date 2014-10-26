package com.smilestudio.wizardescape;

import com.badlogic.gdx.Game;
import com.smilestudio.wizardescape.screen.CoverScreen;


public class WizardEscape extends Game{

    private AdCallback mAdListener;

    public WizardEscape(AdCallback listener) {
        mAdListener = listener;
    }

    @Override
    public void create() {
        CoverScreen coverScreen = new CoverScreen(this, mAdListener);
        setScreen(coverScreen);
    }
}
