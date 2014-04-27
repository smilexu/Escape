package com.smilestudio.wizardescape.model;

public class GameData {
    int mStar = 0;
    boolean mPassed = false;

    public GameData(int star, boolean pass) {
        mStar = star;
        mPassed = pass;
    }
    
    public int getStars() {
        return mStar;
    }
    
    public boolean getPassed() {
        return mPassed;
    }
}
