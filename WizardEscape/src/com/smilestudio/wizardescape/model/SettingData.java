package com.smilestudio.wizardescape.model;

public class SettingData {
    private boolean mHasSoundEffect;
    private boolean mHasMusic;

    public SettingData(boolean hasEffect, boolean hasMusic) {
        mHasSoundEffect = hasEffect;
        mHasMusic = hasMusic;
    }

    public boolean hasSoundEffect() {
        return mHasSoundEffect;
    }

    public boolean hasMusic() {
        return mHasMusic;
    }
}
