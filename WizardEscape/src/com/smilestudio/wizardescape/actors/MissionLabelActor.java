package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MissionLabelActor extends Actor {

   private int mMission;
   private int mSubMission;
   private BitmapFont mBitmapFont;
   private String mLevelString;

public MissionLabelActor(int mission, int subMission) {
       mMission = mission;
       mSubMission = subMission;
       mBitmapFont = new BitmapFont();
       mBitmapFont.setColor(new Color(0.86f, 0.43f, 0.20f, 1f));
       mBitmapFont.setScale(3f);
       mLevelString = mMission + "-" + mSubMission;
   }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        mBitmapFont.draw(batch, mLevelString, getX(), getY());
    }
}
