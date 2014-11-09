package com.smilestudio.wizardescape;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.actors.AdvanceActor;
import com.smilestudio.wizardescape.actors.HeroActor;
import com.smilestudio.wizardescape.actors.KeyActor;
import com.smilestudio.wizardescape.actors.LabelActor;
import com.smilestudio.wizardescape.model.GameData;
import com.smilestudio.wizardescape.screen.GameScreen;
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

    public final static int   COLUMN      = 10;

    public final static int   ROW         = 8;

    public static synchronized GameManager getInstance() {
        if (sInstance == null) {
            sInstance = new GameManager();
        }
        return sInstance;
    }

    /**
     * Map block define 0 - empty, 1 - me, 2 - can NOT be moved, 3 - movable, 4
     * -star, 5 - target, 6 - portal A, 7 - key, 8 - portal B
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
    public final static  String NAME_PROGRESS_TEXT = "progress_text";

    public final static String NAME_BOARD_BG_CIRCLE = "mission_board_bg_circle";
    public final static String NAME_BOARD_CONGRAS_TEXT = "mission_board_congras_text";
    public final static String[] NAME_BOARD_STARS = new String[] {"mission_board_star1", "mission_board_star2", "mission_board_star3"};
    public final static String NAME_BOARD_NEXT = "mission_board_next";

    private final static String PREFERENCES_NAME = "save";

    private int[][]             mMapBlock = null;
    private int                 mMission;
    private int                 mSubmission;
    private Map<Integer, Actor> mActorMap = null;

    private HeroActor           mMe;
    private int                 mCurrentCellX;
    private int                 mCurrentCellY;
    private int                 mStarGot = 0;
    private boolean             mLocked = false;
    private float mMovableObjectWidth;
    private Group mGroup;
    private Group mBkGrdActors;
    private Actor mMask;
    private AdvanceActor mTarget;
    private Group mMissionFinishedBoard;
    private Game mGame;
    private int mStarsTotal;
    private KeyActor mKey;
    private int mSteps;

    public void setMission(int mission, int submission) {
        mMission = mission;
        mSubmission = submission;
        mGame.setScreen(new GameScreen());
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

        mGroup = stage.getRoot();
        mBkGrdActors = bkGrdActors;
        mMask = mask;

        if (mActorMap != null) {
            mActorMap.clear();
        } else {
            mActorMap = new HashMap<Integer, Actor>();
        }

        boolean hasKey = false;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                Actor actor = null;
                final int type = mMapBlock[j][i];
                Vector2 position = MapHelper.getPostionByCell(j, i);
                switch (type) {
                    case TYPE_EMPTY:
                        break;
                    case TYPE_ME:
                        float duration = 0.1f;
                        mMe = new HeroActor(getHeroUpRegions(), duration, getHeroDownRegions(), duration,
                                getHeroLeftRegions(), duration, getHeroRightRegions(), duration,
                                getHeroStandRegions(), duration);
                        mMe.setName(NAME_ME);
                        mCurrentCellX = j;
                        mCurrentCellY = i;
                        actor = (Actor)mMe;
                        actor.setSize(mMe.getWidth(), mMe.getHeight());
                        actor.setPosition(position.x, position.y);
                        break;
                    case TYPE_OBSTACLE:
                        Image obstacle = MapHelper.getObstacleImage(mMission);
                        obstacle.setName(NAME_OBSTACLE);
                        obstacle.setSize(obstacle.getWidth(), obstacle.getHeight());
                        actor = (Actor)obstacle;
                        actor.setPosition(position.x + (Constants.CELL_SIZE_WIDTH - obstacle.getWidth()) / 2, position.y);
                        break;
                    case TYPE_MOVABLE:
                        Image movable = MapHelper.getMovableImage(mMission);
                        movable.setName(NAME_MOVABLE);
                        actor = (Actor)movable;
                        actor.setSize(movable.getWidth(), movable.getHeight());
                        actor.setPosition(position.x + (Constants.CELL_SIZE_WIDTH - movable.getWidth()) / 2, position.y);
                        mMovableObjectWidth = movable.getWidth();
                        break;
                    case TYPE_STAR:
                        AdvanceActor star = generateStarActor();
                        star.setName(NAME_STAR);
                        actor = (Actor)star;
                        actor.setPosition(position.x, position.y);
                        mStarsTotal  = mStarsTotal + 1;
                        break;
                    case TYPE_TARGET:
//                        Image target = new Image(new Texture(Gdx.files.internal("misc/img_target.png")));
                        TextureRegion[] targets = new TextureRegion[4];
                        targets[0] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_1.png")));
                        targets[1] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_2.png")));
                        targets[2] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_3.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_4.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_5.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_6.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_7.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_8.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_9.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_10.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_11.png")));
                        targets[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_target_12.png")));
                        mTarget = new AdvanceActor(0.2f, targets, Animation.LOOP, AdvanceActor.STATUS_PLAY);
                        mTarget.setName(NAME_TARGET);
                        actor = (Actor)mTarget;
                        actor.setSize(Constants.CELL_SIZE_WIDTH, Constants.CELL_SIZE_HEIGHT);
                        actor.setPosition(position.x, position.y);
                        break;
                    case TYPE_PORTAL_A:
                    case TYPE_PORTAL_B:
                        AdvanceActor transport = genaratePortalActor(type);
                        actor = (Actor) transport;
                        actor.setPosition(position.x, position.y);
                        break;
                    case TYPE_KEY:
                        mKey = new KeyActor(new Texture(Gdx.files.internal("misc/img_key.png")),
                                new Texture(Gdx.files.internal("misc/img_key_halo.png")));
                        mKey.setName(NAME_KEY);
                        actor = (Actor)mKey;
                        actor.setPosition(position.x, position.y);
                        mTarget.setStatus(AdvanceActor.STATUS_PAUSE);
                        hasKey = true;
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

        //set target animtion according to key status
        if(hasKey) {
            mLocked = true;
            mTarget.setStatus(AdvanceActor.STATUS_PAUSE);
        } else {
            mLocked = false;
            mTarget.setStatus(AdvanceActor.STATUS_PLAY);
        }

        mGroup.addActorAt(0, bkGrdActors);
        mGroup.addActor(mask);

        updateProgress(bkGrdActors);
    }

    private void updateProgress(Group group) {
        LabelActor progress = (LabelActor) group.findActor(NAME_PROGRESS_TEXT);
        if (progress != null) {
            progress.setContentStr(mStarGot + " / " + mStarsTotal);
        }
    }

    public static AdvanceActor generateStarActor() {
        TextureRegion stars[] = new TextureRegion[6];
        stars[0] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_1.png")));
        stars[1] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_2.png")));
        stars[2] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_3.png")));
        stars[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_4.png")));
        stars[4] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_5.png")));
        stars[5] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_star_6.png")));
        AdvanceActor star = new AdvanceActor(0.2f, stars, Animation.LOOP, AdvanceActor.STATUS_PLAY);
        return star;
    }

    private TextureRegion[] getHeroDownRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_5.png")));
        return regions;
    }

    private TextureRegion[] getHeroUpRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_5.png")));
        return regions;
    }

    private TextureRegion[] getHeroLeftRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_5.png")));
        return regions;
    }

    private TextureRegion[] getHeroRightRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_5.png")));
        return regions;
    }

    private TextureRegion[] getHeroStandRegions() {
        TextureRegion[] regions = new TextureRegion[10];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_5.png")));
        regions[6] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_6.png")));
        regions[7] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_7.png")));
        regions[8] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_8.png")));
        regions[9] = new TextureRegion(new Texture(Gdx.files.internal("character/img_stand_9.png")));
        return regions;
    }

    private AdvanceActor genaratePortalActor(int type) {
        String name;
        TextureRegion[] regions = new TextureRegion[4];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_portal_1.png")));
        if (TYPE_PORTAL_A == type) {
            regions[1] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_portal_blue_2.png")));
            regions[2] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_portal_blue_3.png")));
            regions[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_portal_blue_4.png")));
            name = NAME_PORTAL_A;
        } else {
            regions[1] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_portal_red_2.png")));
            regions[2] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_portal_red_3.png")));
            regions[3] = new TextureRegion(new Texture(Gdx.files.internal("misc/img_portal_red_4.png")));
            name = NAME_PORTAL_B;
        }

        AdvanceActor transport = new AdvanceActor(0.1f, regions, Animation.LOOP_PINGPONG, AdvanceActor.STATUS_PLAY);
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
     * Actor actor: which actor to move
     * boolean isPushStatus: which animation and duration to display
     * boolean updateCellStatus: whether or not update current cell status. Used for overlapped status, such as move from a transport position
     */
    private boolean moveToNextBlock(final int flingDirection, final int cellX, final int cellY, final Actor actor, final boolean isPushStatus,
            final boolean updateCellStatus) {
        int type = getNextActorType(flingDirection, cellX, cellY);

        switch (type) {
            case INVALID:
                if (actor == mMe) {
                    adjustActorsOrder(flingDirection);
                    mInAnimation = false;
                }
                return false;
            case TYPE_ME:
                return false;
            case TYPE_OBSTACLE:
                if (actor == mMe) {
                    adjustActorsOrder(flingDirection);
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

                            generateCongrasAction();
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
                MoveByAction moveBy = Actions.moveBy(0, Constants.ANIMATION_STAR_MOVEBY_Y, Constants.ANIMATION_STAR_DURATION);
                AlphaAction alPha = Actions.alpha(0, Constants.ANIMATION_STAR_DURATION);
                ParallelAction actions = Actions.parallel(moveBy, alPha);
                image.addAction(actions);
                if (TYPE_STAR == type) {
                    mStarGot++;
                    updateProgress(mBkGrdActors);
                } else if (TYPE_KEY == type) {
                    mLocked = false;
                    mTarget.setStatus(AdvanceActor.STATUS_PLAY);
                }
//                if (mStarGot >= 3 && !mLocked) {
//                    mTarget.setStatus(AdvanceActor.STATUS_PLAY);
//                    mLocked = false;
//                }
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
                    mMe.setStatus(flingDirection);
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

                            adjustActorsOrder(flingDirection);
                            mMe.setStatus(HeroActor.STATUS_STAND);
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
     * 1. Mask alpha 50%
     * 2. Background circle drop down to center
     * 3. Congras text drop down to center
     * 4. Stars fly to the center from left side
     * 5. Buttons display from smaller
     */
    private void generateCongrasAction() {
        AlphaAction maskAlphaAction = Actions.alpha(Constants.MISSION_FINISHED_MASK_ALPHA, Constants.MISSION_FINISHED_ALPHA_DURATION);
        mMask.addAction(maskAlphaAction);

        Image bgCircle = (Image) mMissionFinishedBoard.findActor(NAME_BOARD_BG_CIRCLE);
        MoveToAction bgCircleMove = Actions.moveTo(bgCircle.getX(), Constants.MISSION_FINISHED_BG_CIRCLE_Y, Constants.MISSION_FIMISHED_BG_CIRCLE_MOVE_DURATION);

        Image congrasText = (Image) mMissionFinishedBoard.findActor(NAME_BOARD_CONGRAS_TEXT);
        MoveToAction textMove = Actions.moveTo(congrasText.getX(), Constants.MISSION_FINISHED_CONGRAS_TEXT_Y, Constants.MISSION_FIMISHED_BG_CIRCLE_MOVE_DURATION);
        congrasText.addAction(textMove);

        RunnableAction runnable = Actions.run(new Runnable() {

            @Override
            public void run() {
                generateBoardStarsActions(0, 3);
            }});
        
        SequenceAction sequence = Actions.sequence(bgCircleMove, runnable);
        bgCircle.addAction(sequence);
    }

    protected void generateBoardStarsActions(final int current, final int max) {
        if (current > (max - 1)) {
            generateButtonPlayAnimation();
            return;
        }

        Actor star = mMissionFinishedBoard.findActor(NAME_BOARD_STARS[current]);
        MoveToAction moveTo = Actions.moveTo(Constants.MISSION_FINISHED_STAR_X_DEFAULT + Constants.MISSION_FINISHED_STAR_X_DELTA * current, Constants.MISSION_FINISHED_STAR_Y_DEFAULT,
                Constants.MISSION_FINISHED_STAR_FLY_DURATION);
        RunnableAction runnable = Actions.run(new Runnable() {

            @Override
            public void run() {
                generateBoardStarsActions(current + 1, max);
            }});
        SequenceAction sequence = Actions.sequence(moveTo, runnable);
        star.addAction(sequence);
    }

    private void generateButtonPlayAnimation() {
        Image buttonNext = (Image)mMissionFinishedBoard.findActor(NAME_BOARD_NEXT);
        buttonNext.setVisible(true);
        ScaleToAction scaleTo = Actions.scaleTo(1, 1, 0.5f);
        
        RotateByAction rotateBy = Actions.rotateBy(720, 0.5f);
        ParallelAction paralle = Actions.parallel(scaleTo, rotateBy);
        buttonNext.addAction(paralle);
    }

    private void adjustActorsOrder(int flingDirection) {
        if (FLING_UP == flingDirection || FLING_DOWN == flingDirection) {
            mGroup.addActor(mBkGrdActors);

            int index = 0;
            Actor actor = null;
            while (index <= ROW * COLUMN - 1) {
                actor = mActorMap.get(index);
                if (actor != null) {
                    mGroup.addActor(actor);
                }
                index ++;
            }

            mGroup.addActor(mMask);
            mGroup.addActor(mMissionFinishedBoard);
        }
    }

    /**
     * Since every item is center aligned, we should calculate the real position_x
     * @param position The left and bottom position of current cell 
     * @param name The name of current actor
     * @return
     */
    private Vector2 modifyPosition(Vector2 position, String name) {
        if (name.equals(NAME_MOVABLE)) {
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
            case INVALID:
                return false;
            case TYPE_EMPTY:
            case TYPE_MOVABLE:
            case TYPE_STAR:
            case TYPE_TARGET:
        }

        mInAnimation = true;

        MoveToAction transport_moveto = Actions.moveTo(transport_position.x, transport_position.y);
        transport_moveto.setDuration(Constants.ANIMATION_DURATION_PER_BLOCK_NORMAL);
        AlphaAction fadeout = Actions.alpha(0, Constants.ANIMATION_HERO_PORTAL_FADE_DURATION);
        RunnableAction runnable = Actions.run(new Runnable() {

            @Override
            public void run() {
                mMapBlock[cellX][cellY] = TYPE_EMPTY;
                mActorMap.remove(cellY * COLUMN + cellX);
            }

        });
        MoveToAction moveBetweenTransport = Actions.moveTo(otherTransportPosition.x, otherTransportPosition.y);
        moveBetweenTransport.setDuration(0);
        AlphaAction fadein = Actions.alpha(1, Constants.ANIMATION_HERO_PORTAL_FADE_DURATION);
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
        SequenceAction transport_sequence = Actions.sequence(transport_moveto, runnable, fadeout, moveBetweenTransport, fadein, runnableOfNext);
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
            case FLING_DOWN:
                if ((cellY + 1) > ROW) {
                    return null;
                } else {
                    actor = mActorMap.get((cellY + 1) * COLUMN + cellX);
                }
                break;
            case FLING_UP:
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
            case FLING_DOWN:
                if ((cellY + 1) > ROW) {
                    return null;
                } else {
                    result = new Vector2(cellX, cellY + 1);
                }
                break;
            case FLING_UP:
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
            case FLING_DOWN:
                if ((cellY + 1) > ROW) {
                    return null;
                } else {
                    position = MapHelper.getPostionByCell(cellX, cellY + 1);
                }
                break;
            case FLING_UP:
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
            case FLING_DOWN:
                if ((cellY + 1) >= ROW) {
                    return INVALID;
                } else {
                    actor = mActorMap.get((cellY + 1) * COLUMN + cellX);
                }
                break;
            case FLING_UP:
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
        mStarsTotal = 0;
        mSteps = 0;

        initialMissionBoardStatus(mMissionFinishedBoard);
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

    public void setMissionFinishedBoard(Group boardItems) {
        mMissionFinishedBoard = boardItems;
        initialMissionBoardStatus(boardItems);
    }

    private void initialMissionBoardStatus(Group boardItems) {
        if (null == boardItems) {
            return;
        }

        Actor bgCircle = boardItems.findActor(NAME_BOARD_BG_CIRCLE);
        bgCircle.setPosition((mMask.getWidth() - bgCircle.getWidth()) / 2, Constants.STAGE_HEIGHT);

        Actor congrasText = boardItems.findActor(NAME_BOARD_CONGRAS_TEXT);
        congrasText.setPosition((mMask.getWidth() - congrasText.getWidth()) / 2, Constants.STAGE_HEIGHT);

        for (int i = 0; i < NAME_BOARD_STARS.length; i++) {
            Actor star = boardItems.findActor(NAME_BOARD_STARS[i]);
            star.setPosition(0 - star.getWidth(), Constants.STAGE_HEIGHT);
        }

        Image buttonNext = (Image) boardItems.findActor(NAME_BOARD_NEXT);
        buttonNext.setVisible(false);
        buttonNext.setOrigin(buttonNext.getWidth() / 2, buttonNext.getHeight() / 2);
        buttonNext.setScale(Constants.MISSION_FINISHED_BUTTON_NEXT_SCALE_DEFAULT);
    }

    public void gotoNext() {
        if (mMission <= Constants.MISSION_MAX && mSubmission < Constants.SUB_MISSION_MAX) {
            setMission(mMission, mSubmission + 1);
        } else if (mMission < Constants.MISSION_MAX && Constants.SUB_MISSION_MAX == mSubmission) {
            setMission(mMission + 1, 1);
        } else {
            //Do nothing, already pass all missions
        }
    }

    public void setGame(Game game) {
        mGame = game;
    }

    public Game getGame() {
        return mGame;
    }

    public int getSteps() {
        return mSteps;
    }

    
}
