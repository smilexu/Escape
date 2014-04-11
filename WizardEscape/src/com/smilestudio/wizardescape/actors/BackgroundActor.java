package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundActor extends Actor {

    private Texture mBgTexture;

    public BackgroundActor(Texture texture) {
        mBgTexture = texture;
    }
    
    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(mBgTexture, 0, 0);
    }
}
