package com.smilestudio.wizardescape;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.actors.AdvanceActor;
import com.smilestudio.wizardescape.utils.Constants;

public class GameManager {

    private static GameManager sInstance;

    private final static int   COLUMN = 10;

    private final static int   ROW    = 8;

    public static synchronized GameManager getInstance() {
        if (sInstance == null) {
            sInstance = new GameManager();
        }
        return sInstance;
    }

    /**
     * Map block define 
     * 0 - empty
     * 1 - me
     * 2 - can NOT be moved
     * 3 - movable
     * 4 -star
     * 5 - target
     */
    private final static int EMPTY     = 0;
    private final static int ME        = 1;
    private final static int OBSTACLE  = 2;
    private final static int MOVABLE   = 3;
    private final static int STAR      = 4;
    private final static int TARGET    = 5;

    private int[][]          mMapBlock = null;
    private int              mMission;
    private int              mSubmission;
    private Map<Integer, Actor> mActorMap = null;

    public void setMission(int mission, int submission) {
        mMission = mission;
        mSubmission = submission;
    }

    public void initMapBlock() {
        // TODO get data from file
        mMapBlock = new int[COLUMN][ROW];
        for (int i = 0; i < COLUMN; i++) {
            for (int j = 0; j < ROW; j++) {
                mMapBlock[i][j] = 0;
            }
        }
        mMapBlock[3][6] = 1;
        mMapBlock[6][1] = 2;
        mMapBlock[5][3] = 2;
        mMapBlock[7][6] = 2;
        mMapBlock[6][2] = 4;
        mMapBlock[0][6] = 4;
        mMapBlock[6][6] = 4;
        mMapBlock[2][2] = 5;
    }

    public void initActors(Stage stage) {
        if (null == mMapBlock) {
            return;
        }

        mActorMap = new HashMap<Integer, Actor>();
        for (int i = 0; i < COLUMN; i++) {
            for (int j = 0; j < ROW; j++) {
                switch (mMapBlock[i][j]) {
                    case EMPTY:
                        break;
                    case ME:
                        //TODO: support animation
                        Texture texture = new Texture(Gdx.files.internal("character/img_leading_role.png"));
                        TextureRegion region = new TextureRegion(texture, 53, 73);
                        Image leading_role = new Image(region);
                        leading_role.setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
                        leading_role.setPosition(Constants.OFFSET_X + i * Constants.CELL_SIZE,
                                Constants.OFFSET_Y + j * Constants.CELL_SIZE);
                        mActorMap.put(i * COLUMN + j, leading_role);
                        stage.addActor(leading_role);
                        break;
                    case OBSTACLE:
                        Image obstacle = new Image(new Texture(Gdx.files.internal("misc/img_box.png")));
                        obstacle.setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
                        obstacle.setPosition(Constants.OFFSET_X + i * Constants.CELL_SIZE,
                                Constants.OFFSET_Y + j * Constants.CELL_SIZE);
                        mActorMap.put(i * COLUMN + j, obstacle);
                        stage.addActor(obstacle);
                        break;
                    case MOVABLE:
                        Image movable = new Image(new Texture(Gdx.files.internal("misc/img_plate_3.png")));
                        movable.setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
                        movable.setPosition(Constants.OFFSET_X + i * Constants.CELL_SIZE,
                                Constants.OFFSET_Y + j * Constants.CELL_SIZE);
                        mActorMap.put(i * COLUMN + j, movable);
                        stage.addActor(movable);
                        break;
                    case STAR:
                        break;
                    case TARGET:
                        Image target = new Image(new Texture(Gdx.files.internal("misc/img_target.png")));
                        target.setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
                        target.setPosition(Constants.OFFSET_X + i * Constants.CELL_SIZE,
                                Constants.OFFSET_Y + j * Constants.CELL_SIZE);
                        mActorMap.put(i * COLUMN + j, target);
                        stage.addActor(target);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
