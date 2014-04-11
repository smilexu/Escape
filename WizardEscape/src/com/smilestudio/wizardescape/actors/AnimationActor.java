package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimationActor extends Actor {

    private Animation mAnimation;

    private float mStateTime = 0;

    public AnimationActor(float frameDuration, TextureRegion[] regions,
            int playMode) {
        mAnimation = new Animation(frameDuration, regions);
        mAnimation.setPlayMode(playMode);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        mStateTime += Gdx.graphics.getDeltaTime();

        batch.draw(mAnimation.getKeyFrame(mStateTime), getX(), getY());

    }
}
