package com.smilestudio.wizardescape.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.utils.Constants;

public class NumberUnitActor extends Image {

    private String mContent;
    private Texture mUnit;
    //"0123456789-/"
    private Texture[] mNumbers;

    public NumberUnitActor() {
        loadNumberTextures();
    }

    public NumberUnitActor(Texture textureUnit) {
        this();

        mUnit = textureUnit;
    }

    /**
     * Such as "1-1 mission" or "23 steps"
     * @param content The string contains only number and "-"
     * @param textureUnit The unit texture, only support "mission" and "steps"
     */
    public NumberUnitActor(String content, Texture textureUnit) {
        this();

        mContent = content;
        mUnit = textureUnit;
    }

    public void setContent(String content, Texture textureUnit) {
        if (content != null) {
            mContent = content;
        }
        if (textureUnit != null) {
            mUnit = textureUnit;
        }
    }

    private void loadNumberTextures() {
        mNumbers = new Texture[12];
        mNumbers[0] = new Texture(Gdx.files.internal("misc/img_number_0.png"));
        mNumbers[1] = new Texture(Gdx.files.internal("misc/img_number_1.png"));
        mNumbers[2] = new Texture(Gdx.files.internal("misc/img_number_2.png"));
        mNumbers[3] = new Texture(Gdx.files.internal("misc/img_number_3.png"));
        mNumbers[4] = new Texture(Gdx.files.internal("misc/img_number_4.png"));
        mNumbers[4] = new Texture(Gdx.files.internal("misc/img_number_5.png"));
        mNumbers[6] = new Texture(Gdx.files.internal("misc/img_number_6.png"));
        mNumbers[7] = new Texture(Gdx.files.internal("misc/img_number_7.png"));
        mNumbers[8] = new Texture(Gdx.files.internal("misc/img_number_8.png"));
        mNumbers[9] = new Texture(Gdx.files.internal("misc/img_number_9.png"));
        mNumbers[10] = new Texture(Gdx.files.internal("misc/img_number_minus.png"));
        mNumbers[11] = new Texture(Gdx.files.internal("misc/img_number_slash.png"));
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(null == mContent || 0 == mContent.length()) {
            return;
        }

        int x = (int) getX();
        for (int i = 0; i < mContent.length(); i++) {
            switch (mContent.charAt(i)) {
                case '0':
                    batch.draw(mNumbers[0], x, getY());
                    x = x + mNumbers[0].getWidth();
                    break;
                case '1':
                    batch.draw(mNumbers[1], x, getY());
                    x = x + mNumbers[1].getWidth();
                    break;
                case '2':
                    batch.draw(mNumbers[2], x, getY());
                    x = x + mNumbers[2].getWidth();
                    break;
                case '3':
                    batch.draw(mNumbers[3], x, getY());
                    x = x + mNumbers[3].getWidth();
                    break;
                case '4':
                    batch.draw(mNumbers[4], x, getY());
                    x = x + mNumbers[4].getWidth();
                    break;
                case '5':
                    batch.draw(mNumbers[5], x, getY());
                    x = x + mNumbers[5].getWidth();
                    break;
                case '6':
                    batch.draw(mNumbers[6], x, getY());
                    x = x + mNumbers[6].getWidth();
                    break;
                case '7':
                    batch.draw(mNumbers[7], x, getY());
                    x = x + mNumbers[7].getWidth();
                    break;
                case '8':
                    batch.draw(mNumbers[8], x, getY());
                    x = x + mNumbers[8].getWidth();
                    break;
                case '9':
                    batch.draw(mNumbers[9], x, getY());
                    x = x + mNumbers[9].getWidth();
                    break;
                case '-':
                    batch.draw(mNumbers[10], x, getY());
                    x = x + mNumbers[10].getWidth();
                    break;
                case '/':
                    batch.draw(mNumbers[11], x, getY());
                    x = x + mNumbers[11].getWidth();
                    break;
                default:
                    break;
            }
        }

        if (mUnit != null) {
            batch.draw(mUnit, x + Constants.NUMBER_UNIT_ACTOR_UNIT_PADDING, getY());
        }
    }
}
