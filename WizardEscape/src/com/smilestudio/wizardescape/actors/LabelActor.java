package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LabelActor extends Actor {

   private BitmapFont mBitmapFont;
   private String mStrContent;

public LabelActor(String strContent, Color color) {
       mBitmapFont = new BitmapFont();
       mBitmapFont.setColor(color);
       mBitmapFont.setScale(3f);
       mStrContent = strContent;
   }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        if (mStrContent != null) {
            mBitmapFont.draw(batch, mStrContent, getX(), getY());
        }
    }

    public void setContentStr(String content) {
        mStrContent = content;
    }
}
