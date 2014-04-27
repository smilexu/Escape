package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.actors.MissionLabelActor;
import com.smilestudio.wizardescape.utils.Constants;

public class GameScreen implements Screen, GestureListener, EventListener {

    private Game        mGame;
    private Stage       mStage;
    private GameManager mManager;
    private Image mRefreshButton;
    private Group mBackgroudActors;
    private Image mSelectMissionButton;

    public GameScreen(Game game) {
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
        mManager = GameManager.getInstance();
        mManager.initMapBlock();
        mManager.initStatus();

        mStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, true);

        Image bgImage = new Image(new Texture(Gdx.files.internal("background/img_mission1_bk.png")));
        bgImage.setSize(bgImage.getWidth(), bgImage.getHeight());
        bgImage.setPosition(0, 0);
        mStage.addActor(bgImage);

        Texture refreshTexture = new Texture(Gdx.files.internal("buttons/img_restart.png"));
        mRefreshButton = new Image(refreshTexture);
        mRefreshButton.setSize(refreshTexture.getWidth(), refreshTexture.getHeight());
        mRefreshButton.setPosition(Constants.GAME_SCREEN_POSITION_X_REFRESH, Constants.GAME_SCREEN_POSITION_Y_REFRESH);
        mRefreshButton.addListener(this);
        mStage.addActor(mRefreshButton);

        Texture selectTexture = new Texture(Gdx.files.internal("buttons/img_select_mission.png"));
        mSelectMissionButton = new Image(selectTexture);
        mSelectMissionButton.setSize(selectTexture.getWidth(), selectTexture.getHeight());
        mSelectMissionButton.setPosition(Constants.GAME_SCREEN_POSITION_X_SELECT, Constants.GAME_SCREEN_POSITION_Y_SELECT);
        mSelectMissionButton.addListener(this);
        mStage.addActor(mSelectMissionButton);

        MissionLabelActor missionLabel = new MissionLabelActor(mManager.getMission(), mManager.getSubMission());
        missionLabel.setPosition(Constants.GAME_SCREEN_POSITION_X_LEVEL, Constants.GAME_SCREEN_POSITION_Y_LEVEL);
        mStage.addActor(missionLabel);

        mBackgroudActors = new Group();
        mBackgroudActors.addActor(bgImage);
        mBackgroudActors.addActor(mRefreshButton);
        mBackgroudActors.addActor(mSelectMissionButton);
        mBackgroudActors.addActor(missionLabel);
        mManager.initActors(mBackgroudActors, mStage);

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);

    }

    private void resetGame() {
        mManager.initMapBlock();
        mManager.initActors(mBackgroudActors, mStage);
        mManager.initStatus();
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

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector2 stagePoint = mStage.screenToStageCoordinates(new Vector2(x, y));
        if (mRefreshButton == mStage.hit(stagePoint.x, stagePoint.y, true)) {
            resetGame();
            return true;
        } else if (mSelectMissionButton == mStage.hit(stagePoint.x, stagePoint.y, true)) {
            mGame.setScreen(new MissionSelectScreen(mGame));
            return true;
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        int flingDirection = GameManager.FLING_IDLE;
        float value;
        if(Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX < 0) {
                flingDirection = GameManager.FLING_LEFT;
            } else if (velocityX > 0){
                flingDirection = GameManager.FLING_RIGHT;
            }
            value = velocityX;
        } else {
            if (velocityY < 0) {
                flingDirection = GameManager.FLING_UP;
            } else if(velocityY > 0) {
                flingDirection = GameManager.FLING_DOWN;
            }
            value = velocityY;
        }
        
        mManager.onFling(flingDirection, value);
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean handle(Event event) {
        if (mRefreshButton == event.getTarget()) {
            resetGame();
        }
        return true;
    }

}
