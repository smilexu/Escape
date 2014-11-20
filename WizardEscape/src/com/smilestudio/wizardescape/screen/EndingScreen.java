package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.actors.HeroActor;
import com.smilestudio.wizardescape.utils.GameConfig;
import com.smilestudio.wizardescape.utils.ResourceHelper;

public class EndingScreen implements Screen, InputProcessor {

    private Image mMission1;
    private Image mMission2;
    private Image mMission3;
    private HeroActor mHero;
    private Stage mStage;
    private Image mBgMask;
    private Image mHome;
    private Music mBgMusic;
    private Image mEndingText;

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
        loadSounds();

        mStage = new Stage(GameConfig.STAGE_WIDTH, GameConfig.STAGE_HEIGHT, false);

        mMission1 = new Image(new Texture(Gdx.files.internal("background/img_wildfield.png")));
        float internalX = (GameConfig.STAGE_WIDTH - mMission1.getWidth()) / 2;
        mMission1.setPosition(internalX, 0);
        mStage.addActor(mMission1);
        mMission2 = new Image(new Texture(Gdx.files.internal("background/img_desert.png")));
        mMission2.setPosition(internalX, 0);
        mMission3 = new Image(new Texture(Gdx.files.internal("background/img_flowering_cherry.png")));
        mMission3.setPosition(internalX, 0);

        mHome = new Image(new Texture(Gdx.files.internal("background/img_home.png")));
        mHome.setPosition(internalX, 0);

        mEndingText = new Image(new Texture(Gdx.files.internal("text/img_ending_text.png")));
        mEndingText.setPosition((GameConfig.STAGE_WIDTH - mEndingText.getWidth()) / 2, 0 - mEndingText.getHeight());

        mBgMask = new Image(new Texture(Gdx.files.internal("background/img_background_mask.png")));
        mBgMask.setPosition(0, 0);
        mBgMask.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Color color = mBgMask.getColor();
        mBgMask.setColor(new Color(color.r, color.g, color.b, 0f));

        float duration = GameConfig.ANIMATION_HERO_ACTION_DURATION;
        mHero = new HeroActor(ResourceHelper.getHeroUpRegions(), duration, ResourceHelper.getHeroDownRegions(), duration,
                ResourceHelper.getHeroLeftRegions(), duration, ResourceHelper.getHeroRightRegions(), duration,
                ResourceHelper.getHeroStandRegions(), duration);
        mHero.setPosition(internalX, 200);
        mHero.setStatus(HeroActor.STATUS_RIGHT);
        mStage.addActor(mHero);

        final HeroActor dog = new HeroActor(null, GameConfig.ANIMATION_HERO_ACTION_DURATION,
                null, GameConfig.ANIMATION_HERO_ACTION_DURATION,
                ResourceHelper.getDogLeftRegions(), GameConfig.ANIMATION_HERO_ACTION_DURATION,
                ResourceHelper.getDogRightRegions(), GameConfig.ANIMATION_HERO_ACTION_DURATION,
                null, GameConfig.ANIMATION_HERO_ACTION_DURATION);
        dog.setStatus(HeroActor.STATUS_RIGHT);
        dog.setPosition(internalX - 80, 200);
        mStage.addActor(dog);

        RunnableAction switchMission2Bk = Actions.run(new Runnable() {

            @Override
            public void run() {
                mStage.clear();
                mStage.addActor(mMission2);
                mStage.addActor(mHero);
                mStage.addActor(dog);
            }});
        RunnableAction switchMission3Bk = Actions.run(new Runnable() {

            @Override
            public void run() {
                mStage.clear();
                mStage.addActor(mMission3);
                mStage.addActor(mHero);
                mStage.addActor(dog);
            }});
        RunnableAction screenSwitch = Actions.run(new Runnable() {

            @Override
            public void run() {
                switchScreen();
            }});
        SequenceAction seq = Actions.sequence(Actions.moveBy(mMission1.getWidth() / 3, 0, 4f), switchMission2Bk,
                Actions.moveBy(mMission1.getWidth() / 3, 0, 4f), switchMission3Bk,
                Actions.moveBy(mMission1.getWidth() / 3 - 80, 0, 4f), screenSwitch);
        mHero.addAction(seq);

        SequenceAction dogSeq = Actions.sequence(Actions.moveBy(mMission1.getWidth() / 3, 0, 4f),
                Actions.moveBy(mMission1.getWidth() / 3, 0, 4f),
                Actions.moveBy(mMission1.getWidth() / 3 - 80, 0, 3.8f));
        dog.addAction(dogSeq);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    private void loadSounds() {
        mBgMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/music_bk_home.mp3"));
        mBgMusic.setLooping(true);
        mBgMusic.play();
    }

    private void switchScreen() {
        mStage.addActor(mBgMask);
        RunnableAction setupActors = Actions.run(new Runnable () {

            @Override
            public void run() {
                mStage.clear();
                mStage.addActor(mHome);
                float internalX = (GameConfig.STAGE_WIDTH - mMission1.getWidth()) / 2;
                mHero.setPosition(internalX, 200);
                mStage.addActor(mHero);
                mStage.addActor(mBgMask);
            }});
        RunnableAction runToHome = Actions.run(new Runnable() {

            @Override
            public void run() {
                runToHome();
            }});
        SequenceAction fadeinout = Actions.sequence(Actions.alpha(1f, 0.5f), setupActors, Actions.alpha(0f, 0.5f), runToHome);
        mBgMask.addAction(fadeinout);
    }

    private void runToHome() {
        RunnableAction turnToUser = Actions.run(new Runnable() {

            @Override
            public void run() {
                mHero.setStatus(HeroActor.STATUS_STAND);
            }});
        RunnableAction endingText = Actions.run(new Runnable() {

            @Override
            public void run() {
                scrollEndingText();
            }});
        SequenceAction seq = Actions.sequence(Actions.moveBy(600, 0, 6f), turnToUser, endingText);
        mHero.addAction(seq);
    }

    protected void scrollEndingText() {
        mStage.addActor(mEndingText);
        RunnableAction upRun = Actions.run(new Runnable() {

            @Override
            public void run() {
                upRun();
            }});
        SequenceAction scroll = Actions.sequence(Actions.moveBy(0, GameConfig.STAGE_HEIGHT + mEndingText.getHeight(), 10f), upRun);
        mEndingText.addAction(scroll);
    }

    private void upRun() {
        mHero.setStatus(HeroActor.STATUS_UP);
        ParallelAction zoomOutAndFadeOut = Actions.parallel(Actions.alpha(0, 2f), Actions.scaleTo(0.2f, 0.2f, 2f));
        RunnableAction gameover = Actions.run(new Runnable() {

            @Override
            public void run() {
                showGameOver();
            }});
        SequenceAction upAndFadeout = Actions.sequence(Actions.moveBy(0, 180, 4f), zoomOutAndFadeOut, gameover);
        mHero.addAction(upAndFadeout);
    }

    private void showGameOver() {
        mStage.addActor(mBgMask);
        mBgMask.addAction(Actions.sequence(Actions.alpha(1f, 5f), Actions.run(new Runnable() {

            @Override
            public void run() {
                releaseSounds();
                Game game = GameManager.getInstance().getGame();
                game.setScreen(new CoverScreen(game));
            }})));
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
            releaseSounds();
            GameManager.getInstance().getGame().setScreen(new MissionSelectScreen(3));
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
        // TODO Auto-generated method stub
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

    private void releaseSounds() {
        if (mBgMusic.isPlaying()) {
            mBgMusic.stop();
            mBgMusic.dispose();
            mBgMusic = null;
        }
    }

}
