package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.actors.AdvanceActor;
import com.smilestudio.wizardescape.utils.Constants;

public class CoverScreen implements Screen, InputProcessor {

    private Stage mStage;
    private Image mStartButton;
    private Game mGame;

    public CoverScreen(Game game) {
        mGame = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        mStage.act();
        mStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        initResource();

        Gdx.input.setInputProcessor(this);
    }

    private void initResource() {
        Texture logoTexture = new Texture(Gdx.files.internal("character/img_fmls.png"));
        TextureRegion[][] tmpRegions = TextureRegion.split(logoTexture, logoTexture.getWidth() / 2, logoTexture.getHeight() / 2);
        TextureRegion[] logoRegions = new TextureRegion[4];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                logoRegions[index] = tmpRegions[i][j];
                index++;
            }
        }

        mStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, false);
        AdvanceActor logoActor = new AdvanceActor(0.1f, logoRegions, Animation.LOOP_PINGPONG, AdvanceActor.STATUS_PLAY);
        logoActor.setPosition(Constants.COVER_SCREEN_POSITION_X_LOGO, Constants.COVER_SCREEN_POSITION_Y_LOGO);
        logoActor.setSize(logoTexture.getWidth() / 2, logoTexture.getHeight() / 2);
        mStage.addActor(logoActor);

        Texture startTexture = new Texture(Gdx.files.internal("buttons/img_start.png"));
        mStartButton = new Image(startTexture);
        mStartButton.setSize(startTexture.getWidth(), startTexture.getHeight());
        mStartButton.setPosition(Constants.COVER_SCREEN_POSITION_X_START, Constants.COVER_SCREEN_POSITION_Y_START);

        SizeToAction sizeUpAction = Actions.sizeTo(startTexture.getWidth() * 1.1f, startTexture.getHeight() * 1.1f, 0.8f);
        SizeToAction sizeDownAction = Actions.sizeTo(startTexture.getWidth(), startTexture.getHeight(), 0.8f);
        SequenceAction sequence = Actions.sequence(sizeUpAction, sizeDownAction);
        RepeatAction foreverAction = Actions.forever(sequence);
        mStartButton.addAction(foreverAction);
        mStage.addActor(mStartButton);
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        mStage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 stagePoint = mStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        if (mStage.hit(stagePoint.x, stagePoint.y, false) == mStartButton) {
            mGame.setScreen(new MissionSelectScreen(mGame));
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

}
