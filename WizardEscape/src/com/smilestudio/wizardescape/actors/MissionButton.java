package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MissionButton extends Actor {

    private Texture mAvailableButton;
    private Texture mUnavailableButton;
    private TextureRegion mAvailableStar;
    private TextureRegion mUnavailableStar;
    private int           mMission;
    private int           mSubMission;
    private int           mStars;
    private boolean       mAvailable;
    private static int    MAX_STAR_NUMBER = 3;
    private static int    MARGIN_INSIDE = 9;
    
    public static int     ITEM_HEIGHT = 90;

    public MissionButton(Texture availableButton, Texture unavailableButton, TextureRegion availableStar,
            TextureRegion unavailableStar, int mission, int submission, int stars, boolean available) {
        mAvailableButton = availableButton;
        mUnavailableButton = unavailableButton;
        mAvailableStar = availableStar;
        mUnavailableStar = unavailableStar;
        mMission = mission;
        mSubMission = submission;
        mStars = stars;
        mAvailable = available;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        int offset = (mAvailableButton.getWidth() - MAX_STAR_NUMBER * mAvailableStar.getRegionWidth() ) / 2;
        
        for (int i = 0; i < MAX_STAR_NUMBER; i++) {
            if (i < mStars) {
                batch.draw(mAvailableStar, (mAvailableStar.getRegionWidth() + offset) * i + getX(), getY());
            } else {
                batch.draw(mUnavailableStar, (mAvailableStar.getRegionWidth() + offset) * i + getX(), getY());
            }
        }

        if (mAvailable) {
            batch.draw(mAvailableButton, getX(), getY() + MARGIN_INSIDE);
            setTouchable(Touchable.enabled);
        } else {
            batch.draw(mUnavailableButton, getX(), getY() + MARGIN_INSIDE);
            setTouchable(Touchable.disabled);
        }
    }
    
    public int getMission() {
        return mMission;
    }
    
    public int getSubMission() {
        return mSubMission;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        // TODO Auto-generated method stub
        return super.hit(x, y, touchable);
    }
}
