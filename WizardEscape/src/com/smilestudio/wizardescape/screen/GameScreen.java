package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.smilestudio.wizardescape.GameListener;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.actors.AdvanceActor;
import com.smilestudio.wizardescape.actors.ButtonActor;
import com.smilestudio.wizardescape.actors.NumberUnitActor;
import com.smilestudio.wizardescape.model.SettingData;
import com.smilestudio.wizardescape.utils.GameConfig;
import com.smilestudio.wizardescape.utils.ResourceHelper;
import com.smilestudio.wizardescape.utils.ScreenshotFactory;

public class GameScreen implements Screen, GestureListener, EventListener, GameListener {

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
    private NumberUnitActor mStepLabel;
    private Music mBkMusic;
    private Sound mEffectTeleport;
    private Sound mEffectMagicItem;
    private Sound mEffectKey;
    private Sound mEffectPortal;
    private Sound mEffectCheers;
    private Sound mEffectDog;
    private ButtonActor mBtnSoundEffect;
    private ButtonActor mBtnMusic;
    private boolean mHasSoundEffect;
    private boolean mHasMusic;
    private Image mBtnWeibo;
    private Image mBtnWeixin;
    private Sound mEffectTeleportUnvailable;
    private Image mBtnRank;

    public GameScreen() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        mStage.act();
        mStage.draw();
    }

    private void countSteps() {
        mManager.increaseSteps();
        mStepLabel.setContent(String.valueOf(mManager.getSteps()), null);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        mManager = GameManager.getInstance();

        mManager.showMiniBanner(true);

        mManager.initGame();
        mManager.setGameListener(this);

        mStage = new Stage(GameConfig.STAGE_WIDTH, GameConfig.STAGE_HEIGHT, false);

        mBgImage = ResourceHelper.getBgImage(mManager.getMission());
        mBgImage.setSize(mBgImage.getWidth(), mBgImage.getHeight());
        mBgImage.setPosition(0, 0);
        mStage.addActor(mBgImage);

        mGridImage = ResourceHelper.getGridImage(mManager.getMission());
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
        mRefreshButton.setScale(GameConfig.GAME_SCREEN_BTN_SCALE);
        mRefreshButton.setPosition(GameConfig.GAME_SCREEN_POSITION_X_REFRESH, GameConfig.GAME_SCREEN_POSITION_Y_REFRESH);
        mRefreshButton.addListener(this);
        mStage.addActor(mRefreshButton);

        Texture selectTexture = new Texture(Gdx.files.internal("buttons/img_select_mission.png"));
        mSelectMissionButton = new Image(selectTexture);
        mSelectMissionButton.setSize(selectTexture.getWidth(), selectTexture.getHeight());
        mSelectMissionButton.setScale(GameConfig.GAME_SCREEN_BTN_SCALE);
        mSelectMissionButton.setPosition(GameConfig.GAME_SCREEN_POSITION_X_SELECT, GameConfig.GAME_SCREEN_POSITION_Y_SELECT);
        mSelectMissionButton.addListener(this);
        mStage.addActor(mSelectMissionButton);

        NumberUnitActor missionLabel = new NumberUnitActor(mManager.getMission() + "-" + mManager.getSubMission(),
                new Texture(Gdx.files.internal("misc/img_unit_mission.png")));
        missionLabel.setPosition(GameConfig.GAME_SCREEN_POSITION_X_LEVEL, GameConfig.GAME_SCREEN_POSITION_Y_LEVEL);
        mStage.addActor(missionLabel);

        NumberUnitActor progressLabel = new NumberUnitActor();
        progressLabel.setPosition(GameConfig.GAME_SCREEN_POSITION_X_PROGRESS, GameConfig.GAME_SCREEN_POSITION_Y_PROGRESS);
        progressLabel.setName(GameManager.NAME_PROGRESS_TEXT);
        mStage.addActor(progressLabel);

        AdvanceActor starActor = GameManager.generateStarActor();
        starActor.setPosition(GameConfig.GAME_SCREEN_POSITION_X_STAR, GameConfig.GAME_SCREEN_POSITION_Y_STAR);
        mStage.addActor(starActor);

        mStepLabel = new NumberUnitActor(new Texture(Gdx.files.internal("misc/img_unit_step.png")));
        mStepLabel.setPosition(GameConfig.GAME_SCREEN_POSITION_X_STEPS, GameConfig.GAME_SCREEN_POSITION_Y_STEPS);
        mStepLabel.setContent("0", null);
        mStage.addActor(mStepLabel);

        //initial default status according to configuration
        SettingData data = GameManager.getSettingData();
        mHasSoundEffect = data.hasSoundEffect();
        mHasMusic = data.hasMusic();

        Texture textureOn = new Texture(Gdx.files.internal("buttons/img_effect_on.png"));
        mBtnSoundEffect = new ButtonActor(textureOn, new Texture(Gdx.files.internal("buttons/img_effect_off.png")),
                data.hasSoundEffect() ? ButtonActor.STATUS_ON : ButtonActor.STATUS_OFF);
        mBtnSoundEffect.setPosition(GameConfig.GAME_SCREEN_POSITION_X_EFFECT, GameConfig.GAME_SCREEN_POSITION_Y_EFFECT);
        mBtnSoundEffect.setSize(textureOn.getWidth(), textureOn.getHeight());
        mBtnSoundEffect.setScale(GameConfig.GAME_SCREEN_BTN_SCALE);
        mBtnSoundEffect.addListener(this);
        mStage.addActor(mBtnSoundEffect);
   
        textureOn = new Texture(Gdx.files.internal("buttons/img_music_on.png"));
        mBtnMusic = new ButtonActor(textureOn, new Texture(Gdx.files.internal("buttons/img_music_off.png")),
                data.hasMusic() ? ButtonActor.STATUS_ON : ButtonActor.STATUS_OFF);
        mBtnMusic.setPosition(GameConfig.GAME_SCREEN_POSITION_X_MUSIC, GameConfig.GAME_SCREEN_POSITION_Y_MUSIC);
        mBtnMusic.setScale(GameConfig.GAME_SCREEN_BTN_SCALE);
        mBtnMusic.setSize(textureOn.getWidth(), textureOn.getHeight());
        mBtnMusic.addListener(this);
        mStage.addActor(mBtnMusic);

        mBackgroudActors = new Group();
        mBackgroudActors.addActor(mBgImage);
        mBackgroudActors.addActor(mGridImage);
        mBackgroudActors.addActor(mRefreshButton);
        mBackgroudActors.addActor(mSelectMissionButton);
        mBackgroudActors.addActor(missionLabel);
        mBackgroudActors.addActor(progressLabel);
        mBackgroudActors.addActor(starActor);
        mBackgroudActors.addActor(mStepLabel);
        mBackgroudActors.addActor(mBtnSoundEffect);
        mBackgroudActors.addActor(mBtnMusic);
        mManager.initActors(mBackgroudActors, mBgMask, mStage);

        // add mission finished board
        Group missionFinishedBoard = new Group();

        Image bgCircle = ResourceHelper.getCircleImage(mManager.getMission());
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
            star.setScale(1.2f);
            missionFinishedBoard.addActor(star);
        }

        mBtnWeibo = new Image(new Texture(Gdx.files.internal("buttons/img_button_weibo.png")));
        mBtnWeibo.setSize(mBtnWeibo.getWidth(), mBtnWeibo.getHeight());
        mBtnWeibo.setPosition(mBgImage.getWidth() / 2 - 80 - mBtnWeibo.getWidth(), 170);
        mBtnWeibo.setName(GameManager.NAME_BOARD_WEIBO);
        missionFinishedBoard.addActor(mBtnWeibo);

        mBtnNext = new Image(new Texture(Gdx.files.internal("buttons/img_button_next.png")));
        mBtnNext.setSize(mBtnNext.getWidth(), mBtnNext.getHeight());
        mBtnNext.setPosition((mBgImage.getWidth() - mBtnNext.getWidth()) / 2, 170);
        mBtnNext.setName(GameManager.NAME_BOARD_NEXT);
        missionFinishedBoard.addActor(mBtnNext);

        mBtnWeixin = new Image(new Texture(Gdx.files.internal("buttons/img_button_weixin.png")));
        mBtnWeixin.setSize(mBtnWeixin.getWidth(), mBtnWeixin.getHeight());
        mBtnWeixin.setPosition(mBgImage.getWidth() / 2 + 80, 170);
        mBtnWeixin.setName(GameManager.NAME_BOARD_WEIXIN);
        missionFinishedBoard.addActor(mBtnWeixin);

        mBtnRank = new Image(new Texture(Gdx.files.internal("buttons/img_button_rank.png")));
        mBtnRank.setSize(mBtnRank.getWidth(), mBtnRank.getHeight());
        mBtnRank.setPosition(mBgImage.getWidth() - mBtnRank.getWidth() - GameConfig.GAME_SCREEN_BTN_RANK_PADDING,
                GameConfig.GAME_SCREEN_BTN_RANK_PADDING);
        mBtnRank.setName(GameManager.NAME_BOARD_RANK);
        missionFinishedBoard.addActor(mBtnRank);

        mManager.setMissionFinishedBoard(missionFinishedBoard);
        mStage.addActor(missionFinishedBoard);

        loadSounds();

        GestureDetector gd = new GestureDetector(this) {
            @Override
            public boolean keyDown(int keycode) {
                if (Keys.BACK == keycode) {
                    releaseAudio();
                    mManager.getGame().setScreen(new MissionSelectScreen(mManager.getMission()));
                    return true;
                }
                return false;
            }
        };
        Gdx.input.setInputProcessor(gd);
        Gdx.input.setCatchBackKey(true);
    }

    private void loadSounds() {
        if (mBkMusic != null && mBkMusic.isPlaying()) {
            mBkMusic.stop();
            mBkMusic.dispose();
            mBkMusic = null;
        }

        mBkMusic = ResourceHelper.getBkMusic(mManager.getMission());
        mBkMusic.setLooping(true);
        mBkMusic.setVolume(0.5f);
        if (mHasMusic) {
            mBkMusic.play();
        }

        mEffectTeleport = Gdx.audio.newSound(Gdx.files.internal("sound/effect_teleport.wav"));
        mEffectTeleportUnvailable = Gdx.audio.newSound(Gdx.files.internal("sound/effect_warning.mp3"));
        mEffectMagicItem = Gdx.audio.newSound(Gdx.files.internal("sound/effect_magic_item.mp3"));
        mEffectKey = Gdx.audio.newSound(Gdx.files.internal("sound/effect_key.mp3"));
        mEffectPortal = Gdx.audio.newSound(Gdx.files.internal("sound/effect_portal.mp3"));
        mEffectCheers = Gdx.audio.newSound(Gdx.files.internal("sound/effect_cheers.mp3"));
        mEffectDog = Gdx.audio.newSound(Gdx.files.internal("sound/effect_dog.wav"));
    }

    private void resetGame() {
        AlphaAction fadeinAction = Actions.fadeIn(MASK_FADE_DURATION);
        AlphaAction fadeoutAction = Actions.fadeOut(MASK_FADE_DURATION);


        Action resetAction = new Action() {

            @Override
            public boolean act(float delta) {
                mManager.initGame();
                mManager.initActors(mBackgroudActors, mBgMask, mStage);
                mStepLabel.setContent("0", null);

                loadSounds();
                return true;
            }};

        SequenceAction sequence = Actions.sequence(fadeinAction, resetAction, fadeoutAction);
        mBgMask.addAction(sequence);
    }

    @Override
    public void hide() {
        releaseAudio();
        mManager.showMiniBanner(false);
    }

    @Override
    public void pause() {
        releaseAudio();
    }

    @Override
    public void resume() {
        if (mManager != null && mManager.isInGame()) {
            loadSounds();
        }
    }

    @Override
    public void dispose() {
        mStage.dispose();
        releaseAudio();
    }

    private void releaseAudio() {
        if (mBkMusic != null) {
            mBkMusic.stop();
            mBkMusic.dispose();
            mBkMusic = null;
        }

        if (mEffectTeleport != null) {
            mEffectTeleport.dispose();
            mEffectTeleport = null;
        }

        if (mEffectTeleportUnvailable != null) {
            mEffectTeleportUnvailable.dispose();
            mEffectTeleportUnvailable = null;
        }

        if (mEffectMagicItem != null) {
            mEffectMagicItem.dispose();
            mEffectMagicItem = null;
        }

        if (mEffectKey != null) {
            mEffectKey.dispose();
            mEffectKey = null;
        }

        if (mEffectPortal != null) {
            mEffectPortal.dispose();
            mEffectPortal = null;
        }

        if (mEffectCheers != null) {
            mEffectCheers.dispose();
            mEffectCheers = null;
        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector2 stagePoint = mStage.screenToStageCoordinates(new Vector2(x, y));
        Actor actor = mStage.hit(stagePoint.x, stagePoint.y, true);
        if (actor == mRefreshButton) {
            resetGame();
            mManager.breakMission();
            mManager.showScreenAd();
            return true;
        } else if (actor == mSelectMissionButton) {
            mManager.getGame().setScreen(new MissionSelectScreen(mManager.getMission()));
            releaseAudio();
            mManager.breakMission();
            return true;
        } else if (actor == mBtnWeibo) {
            String path = ScreenshotFactory.saveScreenshot();
            mManager.weiboShare(path);
        } else if (actor == mBtnWeixin) {
            String path = ScreenshotFactory.saveScreenshot();
            mManager.weixinShare(path);
        } else if (actor == mBtnNext) {
            releaseAudio();
            mManager.gotoNext();
            return true;
        } else if (actor == mBtnSoundEffect) {
            mHasSoundEffect = (ButtonActor.STATUS_ON == mBtnSoundEffect.toggle());
            GameManager.saveSetting(new SettingData(mHasSoundEffect, mHasMusic));
            return true;
        } else if (actor == mBtnMusic) {
            mHasMusic = ButtonActor.STATUS_ON == mBtnMusic.toggle();
            GameManager.saveSetting(new SettingData(mHasSoundEffect, mHasMusic));
            if (!mHasMusic && mBkMusic != null) {
                mBkMusic.stop();
            } else if (mBkMusic != null){
                mBkMusic.play();
            }
            return true;
        } else if (actor == mBtnRank) {
            mManager.getGame().setScreen(new RankScreen(mManager.generateStepKey(), mManager.getSteps()));
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

        if (mManager.onFling(flingDirection, value)) {
            countSteps();
        }
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
        return false;
    }

    @Override
    public boolean handle(Event event) {

        if (mRefreshButton == event.getTarget()) {
            resetGame();
        }
        return true;
    }

    @Override
    public void onGameSuccess() {
        releaseAudio();
    }

    @Override
    public void onSoundPlay(int type) {
        if (!mHasSoundEffect) {
            return;
        }

        switch (type) {
            case GameListener.TYPE_KEY:
                mEffectKey.play();
                break;
            case GameListener.TYPE_MAGIC_ITEM:
                mEffectMagicItem.play();
                break;
            case GameListener.TYPE_TELEPORT:
                mEffectTeleport.play();
                break;
            case GameListener.TYPE_PORTAL:
                mEffectPortal.play();
                break;
            case GameListener.TYPE_CHEERS:
                mEffectCheers.play();
                break;
            case GameListener.TYPE_DOG:
                mEffectDog.play();
                break;
            case GameListener.TYPE_TELEPORT_UNVAILABLE:
                mEffectTeleportUnvailable.play();
            default:
                break;
        }
    }

}
