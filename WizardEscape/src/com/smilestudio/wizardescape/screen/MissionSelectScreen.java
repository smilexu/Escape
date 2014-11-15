package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.actors.MissionButton;
import com.smilestudio.wizardescape.model.GameData;
import com.smilestudio.wizardescape.utils.Constants;

public class MissionSelectScreen implements Screen, InputProcessor, GestureListener{

    private Stage   mMainStage; //used for all actors except arrows
    private Stage   mArrowStage; //only used for arrows
    private int     mMission;
    private Image mArrowLeft;
    private Image mArrowRight;

    public MissionSelectScreen() {
        this(1);
    }

    public MissionSelectScreen(int mission) {
        GameManager.getInstance().showAdWall();
        mMission = mission;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        mMainStage.act();
        mMainStage.draw();
        if (mMission <= 1) {
            mMission = 1;
            mArrowLeft.setVisible(false);
        } else if (mMission >= Constants.MISSION_MAX) {
            mMission = Constants.MISSION_MAX;
            mArrowRight.setVisible(false);
        } else {
            mArrowLeft.setVisible(true);
            mArrowRight.setVisible(true);
        }
        mArrowStage.act();
        mArrowStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        //initial left & right arrow
        Texture arrowLeftTexture = new Texture(Gdx.files.internal("misc/img_arrow_left.png"));
        mArrowLeft = new Image(arrowLeftTexture);
        mArrowLeft.setSize(arrowLeftTexture.getWidth(), arrowLeftTexture.getHeight());
        mArrowLeft.setPosition(Constants.MISSION_SCREEN_ARROW_PADDING_X, Constants.MISSION_SCREEN_ARROW_Y);

        RepeatAction leftRepeat = Actions.forever(Actions.sequence(Actions.moveBy(20, 0, 0.3f), Actions.moveBy(-20, 0, 0.3f)));
        RepeatAction rightRepeat = Actions.forever(Actions.sequence(Actions.moveBy(-20, 0, 0.3f), Actions.moveBy(20, 0, 0.3f)));
        mArrowLeft.addAction(leftRepeat);

        Texture arrowRightTexture = new Texture(Gdx.files.internal("misc/img_arrow_right.png"));
        mArrowRight = new Image(arrowRightTexture);
        mArrowRight.setSize(arrowRightTexture.getWidth(), arrowRightTexture.getHeight());
        mArrowRight.setPosition(Constants.STAGE_WIDTH - Constants.MISSION_SCREEN_ARROW_PADDING_X - mArrowRight.getWidth(),
                Constants.MISSION_SCREEN_ARROW_Y);
        mArrowRight.addAction(rightRepeat);

        mMainStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, false);
        mArrowStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, false);

        for (int i = 0; i < Constants.MISSION_MAX; i++) {
            setupMissionThumbnail(i);
            setupMissionGroup(i);
        }

        mArrowStage.addActor(mArrowLeft);
        mArrowStage.addActor(mArrowRight);

        //move to current page
        Group group = mMainStage.getRoot();
        group.addAction(Actions.moveBy(0 - Constants.STAGE_WIDTH * (mMission - 1), 0));

        GestureDetector gd = new GestureDetector(this) {
            @Override
            public boolean keyDown(int keycode) {
                if (Keys.BACK == keycode) {
                    GameManager gm = GameManager.getInstance();
                    gm.getGame().setScreen(new CoverScreen(gm.getGame()));
                    return true;
                }
                return false;
            }
        };
        Gdx.input.setInputProcessor(gd);
        Gdx.input.setCatchBackKey(true);
    }

    private void setupMissionThumbnail(int indexOfPage) {
        String path = "misc/img_mission_" + (indexOfPage + 1) + "_thumbnail.png";
        Texture missionTexture = new Texture(Gdx.files.internal(path));
        Image missionImage = new Image(missionTexture);
        missionImage.setSize(missionTexture.getWidth(), missionTexture.getHeight());
        int x = Constants.COVER_SCREEN_DELTA_X_MISSION_THUMBNAIL + indexOfPage * Constants.STAGE_WIDTH;
        missionImage.setPosition(x, (Constants.STAGE_HEIGHT - missionTexture.getHeight()) / 2);
        missionImage.setTouchable(Touchable.disabled);
        mMainStage.addActor(missionImage);
    }

    /**
     * set up mission icons
     * @param indexOfPage indexOfpage + 1 = mission
     */
    private void setupMissionGroup(final int indexOfPage) {
        Texture availableButton = new Texture(Gdx.files.internal("buttons/img_mission_available.png"));
        Texture unavailableButton = new Texture(Gdx.files.internal("buttons/img_mission_unavailable.png"));
        Texture stars = new Texture(Gdx.files.internal("buttons/img_star.png"));
        TextureRegion unavailableStar = new TextureRegion(stars, stars.getWidth() / 2, stars.getHeight());
        TextureRegion availableStar = new TextureRegion(stars, stars.getWidth() / 2, 0, stars.getWidth() / 2, stars.getHeight());

        GameManager gm = GameManager.getInstance();

        boolean first = (0 == indexOfPage);
        GameData currentData = null;
        GameData preData = null;

        for (int i = 0; i < Constants.SUB_MISSION_MAX; i++) {
            //only enable 1-1 mission, others will depends on previous submission
            if (i >= 1) {
                preData = gm.getGameData(indexOfPage + 1, i);
                first = false;
            } else {
                preData = gm.getGameData(indexOfPage, Constants.SUB_MISSION_MAX);
            }
            currentData = gm.getGameData(indexOfPage + 1, i+1);
            boolean passed = first || ((null == currentData) ? false : currentData.getPassed());
            if (preData != null) {
                passed = passed || preData.getPassed();
            }
            MissionButton mb = new MissionButton(availableButton, unavailableButton, availableStar, unavailableStar, indexOfPage + 1, i+1,
                    (null == currentData) ? 0 : currentData.getStars(), Constants.DEBUG ? true : passed);
            mb.setSize(availableButton.getWidth(), MissionButton.ITEM_HEIGHT);
            int indexInRow = i % Constants.MISSION_SCREEN_MAX_COLUMN;
            int indexInColumn = i / Constants.MISSION_SCREEN_MAX_COLUMN;
            mb.setPosition(Constants.MISSION_SCREEN_GROUP_LEFT_TOP_X + (availableButton.getWidth() + Constants.MISSION_SCREEN_GROUP_OFFSET_X) * indexInRow + indexOfPage * Constants.STAGE_WIDTH,
                    Constants.MISSION_SCREEN_GROUP_LEFT_TOP_Y - (MissionButton.ITEM_HEIGHT + Constants.MISSION_SCREEN_GROUP_OFFSET_Y) * indexInColumn);
            mMainStage.addActor(mb);
        }
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
    public boolean keyDown(int keycode) {
        if (Keys.BACK == keycode) {
            GameManager gm = GameManager.getInstance();
            gm.getGame().setScreen(new CoverScreen(gm.getGame()));
            return true;
        }
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
        Vector2 stagePoint = mMainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        Actor actor = mMainStage.hit(stagePoint.x, stagePoint.y, true);

        if (actor instanceof MissionButton) {
            MissionButton mission = (MissionButton)actor;
            GameManager.getInstance().setMission(mission.getMission(), mission.getSubMission());
            return true;
        } else if (null == actor) {
            actor = mArrowStage.hit(stagePoint.x, stagePoint.y, true);
            if (actor == mArrowLeft) {
                mMission = mMission - 1;
                Group group = mMainStage.getRoot();
                group.addAction(Actions.moveBy(Constants.STAGE_WIDTH, 0, 0.5f));
                return true;
            } else if (actor == mArrowRight) {
                mMission = mMission + 1;
                Group group = mMainStage.getRoot();
                group.addAction(Actions.moveBy(-Constants.STAGE_WIDTH, 0, 0.5f));
                return true;
            }
        }
        return false;
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector2 stagePoint = mMainStage.screenToStageCoordinates(new Vector2(x, y));
        Actor actor = mMainStage.hit(stagePoint.x, stagePoint.y, true);

        if (actor instanceof MissionButton) {
            MissionButton mission = (MissionButton)actor;
            GameManager.getInstance().setMission(mission.getMission(), mission.getSubMission());
            return true;
        } else if (null == actor) {
            actor = mArrowStage.hit(stagePoint.x, stagePoint.y, true);
            if (actor == mArrowLeft) {
                mMission = mMission - 1;
                Group group = mMainStage.getRoot();
                group.addAction(Actions.moveBy(Constants.STAGE_WIDTH, 0, 0.5f));
                return true;
            } else if (actor == mArrowRight) {
                mMission = mMission + 1;
                Group group = mMainStage.getRoot();
                group.addAction(Actions.moveBy(-Constants.STAGE_WIDTH, 0, 0.5f));
                return true;
            }
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
        if (Math.abs(velocityX) > 800) {
            if (velocityX > 0 && mMission > 1) {
                mMission = mMission - 1;
                Group group = mMainStage.getRoot();
                group.addAction(Actions.moveBy(Constants.STAGE_WIDTH, 0, 0.5f));
            } else if (velocityX < 0 && mMission < Constants.MISSION_MAX){
                mMission = mMission + 1;
                Group group = mMainStage.getRoot();
                group.addAction(Actions.moveBy(-Constants.STAGE_WIDTH, 0, 0.5f));
            }
            return true;
        }
        return false;
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

}
