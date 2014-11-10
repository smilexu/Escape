package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ButtonActor extends Image {

    private Texture mTextureOn;
    private Texture mTextureOff;
    private Texture mCurrentTexture;
    private int mStatus;
    public final static int STATUS_ON = 0;
    public final static int STATUS_OFF = 1;

    /**
     * set up button status
     * @param on The texture for on status
     * @param off The texture for off status
     * @param status The current status
     */
    public ButtonActor(Texture on, Texture off, int status) {
        mTextureOn = on;
        mTextureOff = off;
        mStatus = status;
        switch (status) {
            case STATUS_ON:
                mCurrentTexture = mTextureOn;
                break;
            case STATUS_OFF:
            default:
                mCurrentTexture = mTextureOff;
                break;
        }
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(mCurrentTexture, getX(), getY());
    }

    /**
     * toggle status
     * @return the status after toggle
     */
    public int toggle() {
        mStatus = (STATUS_ON == mStatus) ? STATUS_OFF : STATUS_ON;
        mCurrentTexture = (mCurrentTexture == mTextureOn) ? mTextureOff : mTextureOn;
        return mStatus;
    }

    public int getStatus() {
        return mStatus;
    }
}
