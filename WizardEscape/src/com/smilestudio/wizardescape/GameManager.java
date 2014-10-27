package com.smilestudio.wizardescape;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.actors.AdvanceActor;
import com.smilestudio.wizardescape.model.GameData;
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
    private final static int    TYPE_PORTAL_A  = 6;
    private final static int    TYPE_KEY       = 7;
    private final static int    TYPE_PORTAL_B  = 8;

    private final static String NAME_ME = "me";
    private final static String NAME_OBSTACLE = "obstacle";
    private final static String NAME_MOVABLE = "movable";
    private final static String NAME_STAR = "star";
    private final static String NAME_TARGET = "target";
    private final static String NAME_PORTAL_A = "portal_a";
    private final static String NAME_KEY = "key";
    private final static String NAME_PORTAL_B = "portal_b";

    private final static String PREFERENCES_NAME = "save";

    private int[][]             mMapBlock = null;
    private int                 mMission;
    private int                 mSubmission;
    private Map<Integer, Actor> mActorMap = null;

    private Image               mMe;
    private int                 mCurrentCellX;
    private int                 mCurrentCellY;
    private int                 mStarGot = 0;
    private boolean             mLocked;
    private float mMovableObjectWidth;

    public void setMission(int mission, int submission) {
        mMission = mission;
        mSubmission = submission;
    }

    public int getMission() {
        return mMission;
    }

    public int getSubMission() {
        return mSubmission;
    }

    private void initMapBlock() {
        if (null == mMapBlock) {
            mMapBlock = new int[COLUMN][ROW];
        }   

        String filePath = "data/" + mMission + "-" + mSubmission;
        FileHandle fd = Gdx.files.internal(filePath);
        if (null == fd) {
            return;
        }

        String content = fd.readString();
        content = content.replace("\n", "");

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                String tmp = String.valueOf(content.charAt(i * COLUMN + j));
                mMapBlock[j][i] = Integer.valueOf(tmp);
                if (TYPE_KEY == mMapBlock[j][i]) {
                    mLocked = true;
                }
            }
        }
    }

    /**
     * Add actors into stage
     * @param bkGrdActors Background actors, these should be added into stage first of all
     * @param mask Used for full screen effect, like fade in / fade out. Must be added at last
     * @param stage
     */
    public void initActors(final Group bkGrdActors, final Actor mask, final Stage stage) {
        if (null == mMapBlock) {
            return;
        }

        if (stage != null) {
            stage.clear();
        }

        stage.addActor(bkGrdActors);

        if (mActorMap != null) {
            mActorMap.clear();
        } else {
            mActorMap = new HashMap<Integer, Actor>();
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                Actor actor = null;
                final int type = mMapBlock[j][i];
                Vector2 position = MapHelper.getPostionByCell(j, i);
                switch (type) {
                    case TYPE_EMPTY:
                        break;
                    case TYPE_ME:
                        // TODO: support animation
                        mMe = new Image(new Texture(Gdx.files.internal("character/img_fake_person.png")));
                        mMe.setName(NAME_ME);
                        mCurrentCellX = j;
                        mCurrentCellY = i;
                        actor = (Actor)mMe;
                        actor.setSize(mMe.getWidth(), mMe.getHeight());
                        actor.setPosition(position.x + (Constants.CELL_SIZE_WIDTH - mMe.getWidth()) / 2, position.y);
                        break;
                    case TYPE_OBSTACLE:
                        Image obstacle = new Image(new Texture(Gdx.files.internal("misc/img_tree1.png")));
                        obstacle.setName(NAME_OBSTACLE);
                        obstacle.setSize(obstacle.getWidth(), obstacle.getHeight());
                        actor = (Actor)obstacle;
                        actor.setPosition(position.x + (Constants.CELL_SIZE_WIDTH - obstacle.getWidth()) / 2, position.y);
                        break;
                    case TYPE_MOVABLE:
                        Image movable = new Image(new Texture(Gdx.files.internal("misc/img_fountain.png")));
                        movable.setName(NAME_MOVABLE);
                        actor = (Actor)movable;
                        actor.setSize(movable.getWidth(), movable.getHeight());
                        actor.setPosition(position.x + (Constants.CELL_SIZE_WIDTH - movable.getWidth()) / 2, position.y);
                        mMovableObjectWidth = movable.getWidth();
                        break;
                    case TYPE_STAR:
//                        Texture texure = new Texture(Gdx.files.internal("misc/img_star.png"));
//                        Image star = new Image(new TextureRegion(texure, 57, 49));
                        TextureRegion regions[] = new TextureRegion[6];
                        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_1.png")));
                        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_2.png")));
                        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_3.png")));
                        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_4.png")));
                        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_5.png")));
                        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_6.png")));
                        AdvanceActor star = new AdvanceActor(0.2f, regions, Animation.LOOP);
                        star.setName(NAME_STAR);
                        actor = (Actor)star;
                        actor.setSize(Constants.CELL_SIZE_WIDTH, Constants.CELL_SIZE_HEIGHT);
                        actor.setPosition(position.x, position.y);
                        break;
                    case TYPE_TARGET:
                        Image target = new Image(new Texture(Gdx.files.internal("misc/img_target.png")));
                        target.setName(NAME_TARGET);
                        actor = (Actor)target;
                        actor.setSize(Constants.CELL_SIZE_WIDTH, Constants.CELL_SIZE_HEIGHT);
                        actor.setPosition(position.x, position.y);
                        break;
                    case TYPE_PORTAL_A:
                    case TYPE_PORTAL_B:
                        AdvanceActor transport = genaratePortalActor(type);
                        actor = (Actor) transport;
                        actor.setSize(Constants.CELL_SIZE_WIDTH, Constants.CELL_SIZE_HEIGHT);
                        actor.setPosition(position.x, position.y);
                        break;
                    case TYPE_KEY:
                        Image key = new Image(new Texture(Gdx.files.internal("misc/img_key.png")));
                        key.setName(NAME_KEY);
                        actor = (Actor)key;
                        actor.setSize(Constants.CELL_SIZE_WIDTH, Constants.CELL_SIZE_HEIGHT);
                        actor.setPosition(position.x, position.y);
                        break;
                    default:
                        break;
                }
                if (null == actor) {
                    continue;
                }

                mActorMap.put(i * COLUMN + j, actor);
                stage.addActor(actor);
            }
        }
        stage.addActor(mask);
    }

    private AdvanceActor genaratePortalActor(int type) {
        Texture tmpTexture = null;
        String name = null;
        if (TYPE_PORTAL_A == type) {
            tmpTexture = new Texture(Gdx.files.internal("misc/img_portal_a.png"));
            name = NAME_PORTAL_A;
        } else {
            tmpTexture = new Texture(Gdx.files.internal("misc/img_portal_b.png"));
            name = NAME_PORTAL_B;
        }

        TextureRegion[][] tmpRegions = TextureRegion.split(tmpTexture, tmpTexture.getWidth() / 3, tmpTexture.getHeight() / 2);
        TextureRegion[] regions = new TextureRegion[6];
        int index = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                regions[index] = tmpRegions[row][col];
                index++;
            }
        }
        AdvanceActor transport = new AdvanceActor(0.1f, regions, Animation.LOOP_PINGPONG);
        transport.setName(name);
        return transport;
    }

    public void onFling(int flingDirection, float value) {
        if (mInAnimation) {
            return;
        }

       moveToNextBlock(flingDirection, mCurrentCellX, mCurrentCellY, mMe, false, true);
    }

    /*
     * int flingDirection: the direction of fling, UP, DOWN, LEFT and RIGHT
     * int cellX: current cell - x
     * int cellY: current cell - y
     * Action actor: which actor to move
     * boolean isPushStatus: which animation and duration to display
     * boolean updateCellStatus: whether or not update current cell status. Used for overlapped status, such as move from a transport position
     */
    private boolean moveToNextBlock(final int flingDirection, final int cellX, final int cellY, final Actor actor, final boolean isPushStatus,
            final boolean updateCellStatus) {
        int type = getNextActorType(flingDirection, cellX, cellY);

        switch (type) {
            case INVALID:
                if (actor == mMe) {
                    mInAnimation = false;
                }
                return false;
            case TYPE_ME:
                return false;
            case TYPE_OBSTACLE:
                if (actor == mMe) {
                    mInAnimation = false;
                }
                return false;
            case TYPE_MOVABLE:
                Actor nextActor = getNextActor(flingDirection, cellX, cellY);
                Vector2 nextCell = getNextBlockCell(flingDirection, cellX, cellY);
                boolean succ = moveToNextBlock(flingDirection, (int)nextCell.x, (int)nextCell.y, nextActor, false, updateCellStatus);
                if (succ) {
                    return moveToNextBlock(flingDirection, cellX, cellY, actor, true, updateCellStatus);
                }
                break;
            case TYPE_TARGET:
                if (mLocked) {
                    return false;
                }
                if (actor == mMe) {
                    mInAnimation = true;
                    mMe.toFront();
                    Vector2 position = getNextBlockPosition(flingDirection, cellX, cellY);
                    MoveToAction moveto = Actions.moveTo(position.x, position.y);
                    moveto.setDuration(Constants.ANIMATION_DURATION_PER_BLOCK_NORMAL);
                    AlphaAction alpha = Actions.alpha(0, Constants.ANIMATION_HERO_PASS);
                    RunnableAction runnable = Actions.run(new Runnable() {

                        @Override
                        public void run() {
                            saveGame();
                        }
                        
                    });
                    SequenceAction sequence = Actions.sequence(moveto, alpha, runnable);
                    mMe.addAction(sequence);
                    return true;
                }
                return false;
            case TYPE_STAR:
            case TYPE_KEY:
                Actor image = (Actor)getNextActor(flingDirection, cellX, cellY);
                image.toFront();
                MoveByAction moveBy = Actions.moveBy(0, Constants.ANIMATION_STAR_MOVEBY, Constants.ANIMATION_STAR_DURATION);
                AlphaAction alPha = Actions.alpha(0, Constants.ANIMATION_STAR_DURATION);
                ParallelAction actions = Actions.parallel(moveBy, alPha);
                image.addAction(actions);
                if (TYPE_STAR == type) {
                    mStarGot++;
                } else if (TYPE_KEY == type) {
                    mLocked = false;
                }
            case TYPE_EMPTY:
                mInAnimation = true;
                Vector2 position = getNextBlockPosition(flingDirection, cellX, cellY);
                position = modifyPosition(position, actor.getName());
                MoveToAction moveto = Actions.moveTo(position.x, position.y);
                if (isPushStatus) {
                    moveto.setDuration(Constants.ANIMATION_DURATION_PER_BLOCK_PUSH);
                } else {
                    moveto.setDuration(Constants.ANIMATION_DURATION_PER_BLOCK_NORMAL);
                }

                SequenceAction sequence;
                if(actor == mMe) {
                    //TODO: refactor this part
                    RunnableAction runnable = Actions.run(new Runnable() {
                        public void run() {
                            Vector2 cell = getNextBlockCell(flingDirection, cellX, cellY);
                            if (updateCellStatus) {
                                mMapBlock[cellX][cellY] = TYPE_EMPTY;
                                mActorMap.remove(cellY * COLUMN + cellX);
                            }

                            mCurrentCellX = (int) cell.x;
                            mCurrentCellY = (int) cell.y;
                            mMapBlock[(int)cell.x][(int)cell.y] = getActorType(actor);
                            mActorMap.put(mCurrentCellY * COLUMN + mCurrentCellX, actor);
                            mInAnimation = false;

                            moveToNextBlock(flingDirection, mCurrentCellX, mCurrentCellY, actor, isPushStatus, true);
                        }

                    });
                    sequence = Actions.sequence(moveto, runnable);
                    actor.addAction(sequence);
                } else {
                    moveto.setDuration(Constants.ANIMATION_DURATION_PER_BLOCK_PUSH);

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
            case TYPE_PORTAL_A:
            case TYPE_PORTAL_B:
                if (actor != mMe) {
                    return false;
                }
                return actionTransportMove(flingDirection, cellX, cellY, type);
        }
        return false;
    }

    /**
     * Since every item is center aligned, we should calculate the real position_x
     * @param position The left and bottom position of current cell 
     * @param name The name of current actor
     * @return
     */
    private Vector2 modifyPosition(Vector2 position, String name) {
        if (name.equals(NAME_ME)) {
            position.x = position.x + (Constants.CELL_SIZE_WIDTH - mMe.getWidth()) / 2;
        } else if (name.equals(NAME_MOVABLE)) {
            position.x = position.x + (Constants.CELL_SIZE_WIDTH - mMovableObjectWidth) / 2;
        }
        return position;
    }

    private boolean actionTransportMove(final int flingDirection, final int cellX, final int cellY, final int type) {
        mMe.toFront();

        Vector2 transport_position = getNextBlockPosition(flingDirection, cellX, cellY);
        Vector2 transport_cell = getNextBlockCell(flingDirection, cellX, cellY);

        final Vector2 otherTransportPosition = getOtherPortalPosition((int)transport_cell.x, (int)transport_cell.y, type);
        final Vector2 otherTransportCell = getOtherPortalCell((int)transport_cell.x, (int)transport_cell.y, type);

        switch (getNextActorType(flingDirection, (int)otherTransportCell.x, (int)otherTransportCell.y)) {
            case TYPE_ME:
            case TYPE_OBSTACLE:
            case TYPE_PORTAL_A:
                return false;
            case TYPE_EMPTY:
            case TYPE_MOVABLE:
            case TYPE_STAR:
            case TYPE_TARGET:
        }

        mInAnimation = true;

        MoveToAction transport_moveto = Actions.moveTo(transport_position.x, transport_position.y);
        transport_moveto.setDuration(Constants.ANIMATION_DURATION_PER_BLOCK_NORMAL);
        RunnableAction runnable = Actions.run(new Runnable() {

            @Override
            public void run() {
                mMapBlock[cellX][cellY] = TYPE_EMPTY;
                mActorMap.remove(cellY * COLUMN + cellX);
            }

        });
        MoveToAction moveBetweenTransport = Actions.moveTo(otherTransportPosition.x, otherTransportPosition.y);
        moveBetweenTransport.setDuration(0);
        Vector2 nextOfOther = getNextBlockPosition(flingDirection, (int)otherTransportCell.x, (int)otherTransportCell.y);
        MoveToAction moveToNextOfOther = Actions.moveTo(nextOfOther.x, nextOfOther.y);
        moveToNextOfOther.setDuration(Constants.ANIMATION_DURATION_PER_BLOCK_NORMAL);
        RunnableAction runnableOfNext = Actions.run(new Runnable() {

            @Override
            public void run() {
                mCurrentCellX = (int) otherTransportCell.x;
                mCurrentCellY = (int) otherTransportCell.y;
                moveToNextBlock(flingDirection, (int)otherTransportCell.x, (int)otherTransportCell.y, mMe, false, false);
            }

        });
        SequenceAction transport_sequence = Actions.sequence(transport_moveto, runnable, moveBetweenTransport, runnableOfNext);
        mMe.addAction(transport_sequence);

        return true;
    }

    private Vector2 getOtherPortalCell(int cellX, int cellY, int type) {
        Vector2 position = null;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (type == mMapBlock[j][i]) {
                    if (j == cellX && i == cellY) {
                        continue;
                    } else {
                        position = new Vector2(j, i);
                    }
                }
            }
        }
        return position;
    }

    private Vector2 getOtherPortalPosition(int cellX, int cellY, int type) {
        Vector2 position = null;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (type == mMapBlock[j][i]) {
                    if (j == cellX && i == cellY) {
                        continue;
                    } else {
                        position = MapHelper.getPostionByCell(j, i);
                    }
                }
            }
        }
        return position;
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
        } else if (actor.getName().equals(NAME_PORTAL_A)) {
            return TYPE_PORTAL_A;
        } else if (actor.getName().equals(NAME_KEY)) {
            return TYPE_KEY;
        } else if (actor.getName().equals(NAME_PORTAL_B)) {
            return TYPE_PORTAL_B;
        }
        return INVALID;
    }

    private void initStatus() {
        mInAnimation = false;
        mStarGot = 0;
        mLocked = false;
    }

    public void saveGame() {
        Preferences prefs = Gdx.app.getPreferences(PREFERENCES_NAME);
        prefs.putInteger(generateStarKey(), mStarGot);
        prefs.putBoolean(generatePassKey(), true);
        prefs.flush();
    }

    public void saveGame(GameData data) {
        Preferences prefs = Gdx.app.getPreferences(PREFERENCES_NAME);
        prefs.putInteger(generateStarKey(), data.getStars());
        prefs.putBoolean(generatePassKey(), data.getPassed());
        prefs.flush();
    }

    public GameData getGameData(int mission, int submission) {
        Preferences prefs = Gdx.app.getPreferences(PREFERENCES_NAME);
        int star = prefs.getInteger(generateStarKey(mission, submission), -1);
        if(-1 == star) {
            return null;
        }
        boolean pass = prefs.getBoolean(generatePassKey(mission, submission), false);
        return new GameData(star, pass);
    }

    private String generateStarKey() {
        return generateStarKey(mMission, mSubmission);
    }
    private String generateStarKey(int mission, int submission) {
        return mission + "-" + submission;
    }

    private String generatePassKey() {
        return generatePassKey(mMission, mSubmission);
    }
    private String generatePassKey(int mission, int submission) {
        return mission + "-" + submission + "-pass";
    }

    public void initGame() {
        initStatus();
        initMapBlock();
    }
}
