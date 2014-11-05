package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.smilestudio.wizardescape.utils.Constants;

public class AdvanceActor extends Actor {

    private Animation mAnimation;

    private float mStateTime = 0;

    private final static int IMAGE = 0;

    private final static int ANIMATION = 1;

    private int mType;

    private TextureRegion mTextureRegion;

    private int mStatus;
    
    public final static int STATUS_PLAY = 1;
    
    public final static int STATUS_PAUSE = 2;

    public AdvanceActor(float frameDuration, TextureRegion[] regions,
            int playMode, int status) {
        mAnimation = new Animation(frameDuration, regions);
        mAnimation.setPlayMode(playMode);
        mType = ANIMATION;
        mStatus = status;
    }

    public AdvanceActor(TextureRegion textureRegion) {
        mTextureRegion = textureRegion;
        mType = IMAGE;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        mStateTime += Gdx.graphics.getDeltaTime();
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if(mType == ANIMATION) {
            if (STATUS_PAUSE == mStatus) {
                TextureRegion firstFrame = mAnimation.getKeyFrame(0);
                batch.draw(firstFrame, getX() + (Constants.CELL_SIZE_WIDTH - firstFrame.getRegionWidth()) / 2, getY());
            } else {
                TextureRegion region = mAnimation.getKeyFrame(mStateTime);
                batch.draw(region, getX() + (Constants.CELL_SIZE_WIDTH - region.getRegionWidth()) / 2, getY());
            }
        } else if(mType == IMAGE) {
            batch.draw(mTextureRegion, getX(), getY());
        }

    }
}
