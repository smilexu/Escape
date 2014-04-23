package com.smilestudio.wizardescape;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.utils.Constants;
import com.smilestudio.wizardescape.utils.MapHelper;

public class GameManager {

    public static final int   FLING_IDLE  = 0;
    public static final int   FLING_UP    = 1;
    public static final int   FLING_DOWN  = 2;
    public static final int   FLING_LEFT  = 3;
    public static final int   FLING_RIGHT = 4;
    
    private static boolean mInAnimation = false;

    private static GameManager sInstance;

    private final static int   COLUMN      = 10;

    private final static int   ROW         = 8;

    public static synchronized GameManager getInstance() {
        if (sInstance == null) {
            sInstance = new GameManager();
        }
        return sInstance;
    }

    /**
     * Map block define 0 - empty 1 - me 2 - can NOT be moved 3 - movable 4
     * -star 5 - target
     */
    private final static int    INVALID  = -1;
    private final static int    TYPE_EMPTY     = 0;
    private final static int    TYPE_ME        = 1;
    private final static int    TYPE_OBSTACLE  = 2;
    private final static int    TYPE_MOVABLE   = 3;
    private final static int    TYPE_STAR      = 4;
    private final static int    TYPE_TARGET    = 5;

    private final static String NAME_ME = "me";
    private final static String NAME_OBSTACLE = "obstacle";
    private final static String NAME_MOVABLE = "movable";
    private final static String NAME_STAR = "star";
    private final static String NAME_TARGET = "target";
    private static final float ANIMATION_DURATION_PER_BLOCK_NORMAL = 0.1f;
    private static final float ANIMATION_DURATION_PER_BLOCK_PUSH = 0.5f;

    private int[][]             mMapBlock = null;
    private int                 mMission;
    private int                 mSubmission;
    private Map<Integer, Actor> mActorMap = null;

    private Image               mMe;
    private int                 mCurrentCellX;
    private int                 mCurrentCellY;

    public void setMission(int mission, int submission) {
        mMission = mission;
        mSubmission = submission;
    }

    public void initMapBlock() {
        // TODO get data from file
        mMapBlock = new int[COLUMN][ROW];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                mMapBlock[j][i] = 0;
            }
        }
        mMapBlock[5][1] = 1;
        mMapBlock[6][6] = 2;
        mMapBlock[5][4] = 2;
        mMapBlock[7][1] = 2;
        mMapBlock[1][1] = 3;
        mMapBlock[2][1] = 3;
        mMapBlock[3][1] = 3;
        mMapBlock[4][1] = 3;
        mMapBlock[6][5] = 4;
        mMapBlock[0][1] = 4;
        mMapBlock[6][1] = 4;
        mMapBlock[2][5] = 5;
    }

    public void initActors(Stage stage) {
        if (null == mMapBlock) {
            return;
        }

        mActorMap = new HashMap<Integer, Actor>();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                Actor actor = null;
                switch (mMapBlock[j][i]) {
                    case TYPE_EMPTY:
                        break;
                    case TYPE_ME:
                        // TODO: support animation
                        Texture texture = new Texture(Gdx.files.internal("character/img_leading_role.png"));
                        TextureRegion region = new TextureRegion(texture, 53, 73);
                        mMe = new Image(region);
                        mMe.setName(NAME_ME);
                        mCurrentCellX = j;
                        mCurrentCellY = i;
                        actor = (Actor)mMe;
                        break;
                    case TYPE_OBSTACLE:
                        Image obstacle = new Image(new Texture(Gdx.files.internal("misc/img_box.png")));
                        obstacle.setName(NAME_OBSTACLE);
                        actor = (Actor)obstacle;
                        break;
                    case TYPE_MOVABLE:
                        Image movable = new Image(new Texture(Gdx.files.internal("misc/img_plate_3.png")));
                        movable.setName(NAME_MOVABLE);
                        actor = (Actor)movable;
                        break;
                    case TYPE_STAR:
                        break;
                    case TYPE_TARGET:
                        Image target = new Image(new Texture(Gdx.files.internal("misc/img_target.png")));
                        target.setName(NAME_TARGET);
                        actor = (Actor)target;
                        stage.addActor(target);
                        break;
                    default:
                        break;
                }
                if (null == actor) {
                    continue;
                }
                actor.setSize(Constants.CELL_SIZE, Constants.CELL_SIZE);
                Vector2 position = MapHelper.getPostionByCell(j, i);
                actor.setPosition(position.x, position.y);
                mActorMap.put(i * COLUMN + j, actor);
                stage.addActor(actor);
            }
        }
    }

    public void onFling(int flingDirection, float value) {
        if (mInAnimation) {
            return;
        }

       moveToNextBlock(flingDirection, mCurrentCellX, mCurrentCellY, mMe);
    }

    private boolean moveToNextBlock(final int flingDirection, final int cellX, final int cellY, final Actor actor) {
        int type = getNextActorType(flingDirection, cellX, cellY);

        switch (type) {
            case INVALID:
                return false;
            case TYPE_ME:
                return false;
            case TYPE_OBSTACLE:
                return false;
            case TYPE_MOVABLE:
                //TODO: max move 2 movable one time, need while ?
                Actor nextActor = getNextActor(flingDirection, cellX, cellY);
                Vector2 nextCell = getNextBlockCell(flingDirection, cellX, cellY);
                boolean succ = moveToNextBlock(flingDirection, (int)nextCell.x, (int)nextCell.y, nextActor);
                if (succ) {
                    return moveToNextBlock(flingDirection, cellX, cellY, actor);
                }
                break;
            case TYPE_STAR:
                break;
            case TYPE_TARGET:
                mInAnimation = false;
                return false;
            case TYPE_EMPTY:
                mInAnimation = true;
                Vector2 position = getNextBlockPosition(flingDirection, cellX, cellY);
                MoveToAction moveto = Actions.moveTo(position.x, position.y);
                moveto.setDuration(ANIMATION_DURATION_PER_BLOCK_NORMAL);
                SequenceAction sequence;
                if(actor == mMe) {
                    //TODO: refactor this part
                    RunnableAction runnable = Actions.run(new Runnable() {
                        public void run() {
                            Vector2 cell = getNextBlockCell(flingDirection, cellX, cellY);
                            mMapBlock[cellX][cellY] = TYPE_EMPTY;
                            mActorMap.remove(cellY * COLUMN + cellX);

                            mCurrentCellX = (int) cell.x;
                            mCurrentCellY = (int) cell.y;
                            mMapBlock[(int)cell.x][(int)cell.y] = getActorType(actor);
                            mActorMap.put(mCurrentCellY * COLUMN + mCurrentCellX, actor);

                            mInAnimation = false;

                            moveToNextBlock(flingDirection, mCurrentCellX, mCurrentCellY, actor);
                        }

                    });
                    sequence = Actions.sequence(moveto, runnable);
                    actor.addAction(sequence);
                } else {
                    //if the moved block is not hero, we just leave a empty at previous position,
                    //to let the hero goes into
                    Vector2 cell = getNextBlockCell(flingDirection, cellX, cellY);
                    mMapBlock[cellX][cellY] = TYPE_EMPTY;
                    mActorMap.remove(cellY * COLUMN + cellX);

                    mMapBlock[(int) cell.x][(int) cell.y] = getActorType(actor);
                    mActorMap.put((int)cell.y * COLUMN + (int)cell.x, actor);
                    
                    mInAnimation = false;
                    
                    sequence = Actions.sequence(moveto);
                    actor.addAction(moveto);
                }
                return true;
        }
        return false;
    }

    private Actor getNextActor(int direction, int cellX, int cellY) {
        Actor actor = null;
        switch (direction) {
            case FLING_UP:
                if ((cellY + 1) > ROW) {
                    return null;
                } else {
                    actor = mActorMap.get((cellY + 1) * COLUMN + cellX);
                }
                break;
            case FLING_DOWN:
                if ((cellY - 1) < 0) {
                    return null;
                } else {
                    actor = mActorMap.get((cellY - 1) * COLUMN + cellX);
                }
                break;
            case FLING_LEFT:
                if ((cellX - 1) < 0) {
                    return null;
                } else {
                    actor = mActorMap.get(cellY * COLUMN + (cellX - 1));
                }
                break;
            case FLING_RIGHT:
                if ((cellX + 1) > COLUMN) {
                    return null;
                } else {
                    actor = mActorMap.get(cellY * COLUMN + (cellX + 1));
                }
                break;
            default:
                return null;
        }
        return actor;
    }

    private Vector2 getNextBlockCell(final int direction, final int cellX, final int cellY) {
        Vector2 result = null;
        switch (direction) {
            case FLING_UP:
                if ((cellY + 1) > ROW) {
                    return null;
                } else {
                    result = new Vector2(cellX, cellY + 1);
                }
                break;
            case FLING_DOWN:
                if ((cellY - 1) < 0) {
                    return null;
                } else {
                    result = new Vector2(cellX, cellY - 1);
                }
                break;
            case FLING_LEFT:
                if ((cellX - 1) < 0) {
                    return null;
                } else {
                    result = new Vector2(cellX - 1, cellY);
                }
                break;
            case FLING_RIGHT:
                if ((cellX + 1) > COLUMN) {
                    return null;
                } else {
                    result = new Vector2(cellX + 1, cellY);
                }
                break;
            default:
                return null;
        }
        return result;
    }

    private Vector2 getNextBlockPosition(final int direction, final int cellX, final int cellY) {
        Vector2 position = null;
        switch (direction) {
            case FLING_UP:
                if ((cellY + 1) > ROW) {
                    return null;
                } else {
                    position = MapHelper.getPostionByCell(cellX, cellY + 1);
                }
                break;
            case FLING_DOWN:
                if ((cellY - 1) < 0) {
                    return null;
                } else {
                    position = MapHelper.getPostionByCell(cellX, cellY - 1);
                }
                break;
            case FLING_LEFT:
                if ((cellX - 1) < 0) {
                    return null;
                } else {
                    position = MapHelper.getPostionByCell(cellX - 1, cellY);
                }
                break;
            case FLING_RIGHT:
                if ((cellX + 1) > COLUMN) {
                    return null;
                } else {
                    position = MapHelper.getPostionByCell(cellX + 1, cellY);
                }
                break;
            case FLING_IDLE:
            default:
                return null;
        }

        return position;
    }

    private int getNextActorType(int direction, int cellX, int cellY) {
        Actor actor = null;
        switch (direction) {
            case FLING_UP:
                if ((cellY + 1) >= ROW) {
                    return INVALID;
                } else {
                    actor = mActorMap.get((cellY + 1) * COLUMN + cellX);
                }
                break;
            case FLING_DOWN:
                if ((cellY - 1) < 0) {
                    return INVALID;
                } else {
                    actor = mActorMap.get((cellY - 1) * COLUMN + cellX);
                }
                break;
            case FLING_LEFT:
                if ((cellX - 1) < 0) {
                    return INVALID;
                } else {
                    actor = mActorMap.get(cellY * COLUMN + cellX - 1);
                }
                break;
            case FLING_RIGHT:
                if ((cellX + 1) >= COLUMN) {
                    return INVALID;
                } else {
                    actor = mActorMap.get(cellY * COLUMN + cellX + 1);
                }
                break;
            case FLING_IDLE:
            default:
                    return INVALID;
        }

        return getActorType(actor);
    }

    private int getActorType(Actor actor) {
        if (null == actor) {
            return TYPE_EMPTY;
        } else if (actor.getName().equals(NAME_ME)) {
            return TYPE_ME;
        } else if (actor.getName().equals(NAME_MOVABLE)) {
            return TYPE_MOVABLE;
        } else if (actor.getName().equals(NAME_OBSTACLE)) {
            return TYPE_OBSTACLE;
        } else if (actor.getName().equals(NAME_STAR)) {
            return TYPE_STAR;
        } else if (actor.getName().equals(NAME_TARGET)) {
            return TYPE_TARGET;
        }
        return INVALID;
    }
}
