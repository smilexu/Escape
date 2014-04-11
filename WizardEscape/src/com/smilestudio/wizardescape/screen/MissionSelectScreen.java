package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.utils.Constants;

public class MissionSelectScreen implements Screen {

    private Game mGame;
    private Stage mStage;

    public MissionSelectScreen(Game game) {
        mGame = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // TODO Auto-generated method stub

        mStage.act();
        
        mStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        Texture mission1Texture = new Texture(Gdx.files.internal("misc/img_mission1_thumbnail.png"));
        Image mission1Image = new Image(mission1Texture);
        mission1Image.setSize(mission1Texture.getWidth(), mission1Texture.getHeight());
        mission1Image.setPosition(150, 180);
        
        mStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, true);
        mStage.addActor(mission1Image);
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

    }

}
