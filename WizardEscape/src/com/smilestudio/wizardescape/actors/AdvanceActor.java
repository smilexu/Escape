package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AdvanceActor extends Actor {

    private Animation mAnimation;

    private float mStateTime = 0;

    private final static int IMAGE = 0;

    private final static int ANIMATION = 1;

    private int mType;

    private Vector2 mMapPosition = new Vector2();;

    private TextureRegion mTextureRegion;

    public AdvanceActor(float frameDuration, TextureRegion[] regions,
            int playMode) {
        mAnimation = new Animation(frameDuration, regions);
        mAnimation.setPlayMode(playMode);
        mType = ANIMATION;
    }

    public AdvanceActor(TextureRegion textureRegion) {
        mTextureRegion = textureRegion;
        mType = IMAGE;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        mStateTime += Gdx.graphics.getDeltaTime();

        if(mType == ANIMATION) {
            batch.draw(mAnimation.getKeyFrame(mStateTime), getX(), getY());
        } else if(mType == IMAGE) {
            batch.draw(mTextureRegion, getX(), getY());
        }

    }

    public void setMapPostion(int column, int row) {
        mMapPosition.set(column, row);
    }

    public Vector2 getMapPostion() {
        return mMapPosition;
    }
}
