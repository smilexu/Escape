package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.utils.Constants;

public class KeyActor extends Actor {

    private Texture mKeyTexture;
    private Image mHaloImg;
    private float mTimeState;
    private RotateToAction mHaloAction = Actions.rotateTo(0);

    private static final int ROTATE_SPEED = 30;
    private static final int DELTA_X = 27;
    private static final int DELTA_Y = -20;

    public KeyActor(Texture keyTexture, Texture haloTexture) {
        mKeyTexture = keyTexture;
        mHaloImg = new Image(haloTexture);
        mHaloImg.setOrigin(mHaloImg.getWidth() / 2, mHaloImg.getHeight() / 2);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.draw(mKeyTexture, getX() + (Constants.CELL_SIZE_WIDTH - mKeyTexture.getWidth()) / 2, getY());
        mHaloImg.draw(batch, color.a * parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        mTimeState = mTimeState + delta;
        mHaloAction.restart();
        mHaloAction.setRotation(mTimeState * ROTATE_SPEED);
        mHaloImg.addAction(mHaloAction);
        mHaloImg.setPosition(getX() + DELTA_X, getY() + DELTA_Y);
        mHaloImg.act(delta);
    }
}
