package com.smilestudio.wizardescape.model;

public class GameData {
    int mStar = 0;
    boolean mPassed = false;
    int mSteps = 0;

    public GameData(int star, boolean pass, int steps) {
        mStar = star;
        mPassed = pass;
        mSteps = steps;
    }

    public int getStars() {
        return mStar;
    }
    
    public boolean getPassed() {
        return mPassed;
    }

    public int getSteps() {
        return mSteps;
    }
}
