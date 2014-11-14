package com.smilestudio.wizardescape;

import com.badlogic.gdx.Game;
import com.smilestudio.wizardescape.screen.CoverScreen;


public class WizardEscape extends Game{

    @Override
    public void create() {
        setScreen(new CoverScreen(this));
    }

    public void setAnalyticsListener(AnalyticsListener listener) {
        GameManager.getInstance().setAnalyticsListener(listener);
    }

    public void setAdListener(AdListener listener) {
        GameManager.getInstance().setAdListener(listener);
    }

}
