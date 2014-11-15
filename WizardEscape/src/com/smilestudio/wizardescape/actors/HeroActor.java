package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.smilestudio.wizardescape.utils.Constants;

public class HeroActor extends Actor {

    private Animation mUpAnimation;
    private Animation mDownAnimation;
    private Animation mLeftAnimation;
    private Animation mRightAnimation;
    private Animation mStandAnimation;

    private int mStatus;
    private float mStateTime;
    public static final int STATUS_STAND = 0;
    public static final int STATUS_UP = 1;
    public static final int STATUS_DOWN = 2;
    public static final int STATUS_LEFT = 3;
    public static final int STATUS_RIGHT = 4;

    public HeroActor(TextureRegion[] upRegions, float upDuration,
                    TextureRegion[] downRegions, float downDuration,
                    TextureRegion[] leftRegions, float leftDuration,
                    TextureRegion[] rightRegions, float rightDuration,
                    TextureRegion[] standRegions, float standDuration) {
        if (upRegions != null) {
            mUpAnimation = new Animation(upDuration, upRegions);
            mUpAnimation.setPlayMode(Animation.LOOP);
        }
        if (downRegions != null) {
            mDownAnimation = new Animation(downDuration, downRegions);
            mDownAnimation.setPlayMode(Animation.LOOP);
        }
        if (leftRegions != null) {
            mLeftAnimation = new Animation(leftDuration, leftRegions);
            mLeftAnimation.setPlayMode(Animation.LOOP);
        }
        if (rightRegions != null) {
            mRightAnimation = new Animation(rightDuration, rightRegions);
            mRightAnimation.setPlayMode(Animation.LOOP);
        }
        if (standRegions != null) {
            mStandAnimation = new Animation(standDuration, standRegions);
            mStandAnimation.setPlayMode(Animation.LOOP);
        }

        if (mStandAnimation != null) {
            mStatus = STATUS_STAND;
        } else {
            mStatus = STATUS_LEFT;
        }
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

        float scaleX = getScaleX();
        float scaleY = getScaleY();

        TextureRegion region = null;
        switch (mStatus) {
            case STATUS_UP:
                region = mUpAnimation.getKeyFrame(mStateTime);
                break;
            case STATUS_DOWN:
                region = mDownAnimation.getKeyFrame(mStateTime);
                break;
            case STATUS_LEFT:
                region = mLeftAnimation.getKeyFrame(mStateTime);
                break;
            case STATUS_RIGHT:
                region = mRightAnimation.getKeyFrame(mStateTime);
                break;
            case STATUS_STAND:
            default:
                region = mStandAnimation.getKeyFrame(mStateTime);
                break;
        }
        batch.draw(region, getX() + (Constants.CELL_SIZE_WIDTH - region.getRegionWidth() * scaleX) / 2, getY(),
                region.getRegionWidth() * scaleX, region.getRegionHeight() * scaleY);
    }

}
