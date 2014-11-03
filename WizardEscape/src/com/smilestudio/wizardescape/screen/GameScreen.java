package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.actors.MissionLabelActor;
import com.smilestudio.wizardescape.utils.Constants;
import com.smilestudio.wizardescape.utils.MapHelper;

public class GameScreen implements Screen, GestureListener, EventListener {

    private static final float MASK_FADE_DURATION = 1f;
    private Stage       mStage;
    private GameManager mManager;
    private Image mRefreshButton;
    private Group mBackgroudActors;
    private Image mSelectMissionButton;
    private Image mBgImage;
    private Image mBgMask;
    private Image mGridImage;
    private Image mBtnNext;

    public GameScreen() {
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
        mManager.initGame();

        mStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, false);

        mBgImage = MapHelper.getBgImage(mManager.getMission());
        mBgImage.setSize(mBgImage.getWidth(), mBgImage.getHeight());
        mBgImage.setPosition(0, 0);
        mStage.addActor(mBgImage);

        mGridImage = MapHelper.getGridImage(mManager.getMission());
        mGridImage.setSize(mGridImage.getWidth(), mGridImage.getHeight());
        mGridImage.setPosition(0, 0);
        mStage.addActor(mGridImage);

        //initial background mask， used for full screen effect, such as fade in / fade out
        mBgMask = new Image(new Texture(Gdx.files.internal("background/img_background_mask.png")));
        mBgMask.setSize(mBgImage.getWidth(), mBgImage.getHeight());
        mBgMask.setPosition(0, 0);
        Color color = mBgMask.getColor();
        mBgMask.setColor(new Color(color.r, color.g, color.b, 0f));

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
        mBackgroudActors.addActor(mBgImage);
        mBackgroudActors.addActor(mGridImage);
        mBackgroudActors.addActor(mRefreshButton);
        mBackgroudActors.addActor(mSelectMissionButton);
        mBackgroudActors.addActor(missionLabel);
        mManager.initActors(mBackgroudActors, mBgMask, mStage);

        // add mission finished board
        Group missionFinishedBoard = new Group();

        Image bgCircle = MapHelper.getCircleImage(mManager.getMission());
        bgCircle.setSize(bgCircle.getWidth(), bgCircle.getHeight());
        bgCircle.setName(GameManager.NAME_BOARD_BG_CIRCLE);
        missionFinishedBoard.addActor(bgCircle);

        Image congrasText = new Image(new Texture(Gdx.files.internal("misc/img_congras_relief.png")));
        congrasText.setSize(congrasText.getWidth(), congrasText.getHeight());
        congrasText.setName(GameManager.NAME_BOARD_CONGRAS_TEXT);
        missionFinishedBoard.addActor(congrasText);

        Texture starTexture = new Texture(Gdx.files.internal("misc/img_score_star_relief.png"));

        for (int i = 0; i < GameManager.NAME_BOARD_STARS.length; i++) {
            Image star = new Image(starTexture);
            star.setSize(starTexture.getWidth(), starTexture.getHeight());
            star.setName(GameManager.NAME_BOARD_STARS[i]);
            missionFinishedBoard.addActor(star);
        }

        mBtnNext = new Image(new Texture(Gdx.files.internal("misc/img_button_next_relief.png")));
        mBtnNext.setSize(mBtnNext.getWidth(), mBtnNext.getHeight());
        mBtnNext.setPosition((mBgImage.getWidth() - mBtnNext.getWidth()) / 2, 150);
        mBtnNext.setName(GameManager.NAME_BOARD_NEXT);
        missionFinishedBoard.addActor(mBtnNext);

        mManager.setMissionFinishedBoard(missionFinishedBoard);
        mStage.addActor(missionFinishedBoard);

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    private void resetGame() {
        AlphaAction fadeinAction = Actions.fadeIn(MASK_FADE_DURATION);
        AlphaAction fadeoutAction = Actions.fadeOut(MASK_FADE_DURATION);


        Action resetAction = new Action() {

            @Override
            public boolean act(float delta) {
                mManager.initGame();
                mManager.initActors(mBackgroudActors, mBgMask, mStage);
                return true;
            }};

        SequenceAction sequence = Actions.sequence(fadeinAction, resetAction, fadeoutAction);
        mBgMask.addAction(sequence);
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
        Actor actor = mStage.hit(stagePoint.x, stagePoint.y, true);
        if (actor == mRefreshButton) {
            resetGame();
            return true;
        } else if (actor == mSelectMissionButton) {
            mManager.getGame().setScreen(new MissionSelectScreen(mManager.getMission()));
            return true;
        } else if(actor == mBtnNext) {
            mManager.gotoNext();
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
