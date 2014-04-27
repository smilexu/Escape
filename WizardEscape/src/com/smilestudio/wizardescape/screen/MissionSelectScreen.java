package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.actors.MissionButton;
import com.smilestudio.wizardescape.model.GameData;
import com.smilestudio.wizardescape.utils.Constants;

public class MissionSelectScreen implements Screen, InputProcessor {

    private Game    mGame;
    private Stage   mStage;
    private int     mMission;

    public MissionSelectScreen(Game game) {
        this(game, 1);
    }

    public MissionSelectScreen(Game game, int mission) {
        mGame = game;
        mMission = mission;
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
        mission1Image.setPosition(100, (Constants.STAGE_HEIGHT - mission1Texture.getHeight()) / 2);
        mission1Image.setTouchable(Touchable.disabled);

        mStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, true);
        mStage.addActor(mission1Image);

        initMissionGroup();

        Gdx.input.setInputProcessor(this);
    }

    private void initMissionGroup() {
        Texture availableButton = new Texture(Gdx.files.internal("buttons/img_mission_available.png"));
        Texture unavailableButton = new Texture(Gdx.files.internal("buttons/img_mission_unavailable.png"));
        Texture stars = new Texture(Gdx.files.internal("buttons/img_star.png"));
        TextureRegion unavailableStar = new TextureRegion(stars, stars.getWidth() / 2, stars.getHeight());
        TextureRegion availableStar = new TextureRegion(stars, stars.getWidth() / 2, 0, stars.getWidth() / 2, stars.getHeight());

        GameManager gm = GameManager.getInstance();

        boolean first = true;
        GameData currentData = null;
        GameData preData = null;

        for (int i = 0; i < Constants.MISSION_SCREEN_MAX_BUTTONS; i++) {
            if (i >= 1) {
                preData = gm.getGameData(mMission, i);
                first = false;
            }
            currentData = gm.getGameData(mMission, i+1);
            boolean passed = first || ((null == currentData) ? false : currentData.getPassed());
            if (preData != null) {
                passed = passed || preData.getPassed();
            }
            MissionButton mb = new MissionButton(availableButton, unavailableButton, availableStar, unavailableStar, mMission, i+1,
                    (null == currentData) ? 0 : currentData.getStars(), passed);
            mb.setSize(availableButton.getWidth(), MissionButton.ITEM_HEIGHT);
            int indexInRow = i % Constants.MISSION_SCREEN_MAX_COLUMN;
            int indexInColumn = i / Constants.MISSION_SCREEN_MAX_COLUMN;
            mb.setPosition(Constants.MISSION_SCREEN_GROUP_LEFT_TOP_X + (availableButton.getWidth() + Constants.MISSION_SCREEN_GROUP_OFFSET_X) * indexInRow,
                    Constants.MISSION_SCREEN_GROUP_LEFT_TOP_Y - (MissionButton.ITEM_HEIGHT + Constants.MISSION_SCREEN_GROUP_OFFSET_Y) * indexInColumn);
            mStage.addActor(mb);
        }

//        MissionButton mb1 = new MissionButton(availableButton, unavailableButton, availableStar, unavailableStar, 1, 1, 3, true);
//        mb1.setSize(availableButton.getWidth(), MissionButton.ITEM_HEIGHT);
//        mb1.setPosition(400, 300);
//        mb1.setTouchable(Touchable.enabled);
//        mStage.addActor(mb1);
//
//        MissionButton mb2 = new MissionButton(availableButton, unavailableButton, availableStar, unavailableStar, 1, 1, 0, false);
//        mb2.setSize(availableButton.getWidth(), MissionButton.ITEM_HEIGHT);
//        mb2.setPosition(500, 300);
//        mb2.setTouchable(Touchable.disabled);
//        mStage.addActor(mb2);
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
        MissionButton mission = (MissionButton)mStage.hit(stagePoint.x, stagePoint.y, true);

        if (mission != null) {
            GameManager.getInstance().setMission(mission.getMission(), mission.getSubMission());
            mGame.setScreen(new GameScreen(mGame));
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
