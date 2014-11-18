package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.utils.Constants;

public class StarttingScreen implements Screen, InputProcessor {

    private Stage mStage;
    private Image mPotionGreen;
    private Image mPotionPurple;
    private Image mSkip;
    private Image mText_1;
    private Image mText_2;
    private Image mText_3;
    private Image mText_4;
    private Image mBomb;
    private Sound mEffectBomb;
    private Image mSketch;
    private Music mMusicGlass;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        mStage.act();
        mStage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        mStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, false);

        mMusicGlass = Gdx.audio.newMusic(Gdx.files.internal("sound/effect_glass.mp3"));
        mMusicGlass.play();

        mPotionGreen = new Image(new Texture(Gdx.files.internal("misc/img_potion_green.png")));
        mPotionGreen.setPosition(Constants.STAGE_WIDTH / 2 - 200, (Constants.STAGE_HEIGHT - mPotionGreen.getHeight()) / 2);
        mPotionGreen.setOrigin(mPotionGreen.getWidth() / 2, mPotionGreen.getHeight() / 2);
        mPotionGreen.setRotation(-Constants.STARTTING_SCREEN_POTION_ROTATE);
        RepeatAction repeat = Actions.repeat(Constants.STARTTING_SCREEN_POTION_ROTATE_COUNT,
                Actions.sequence(Actions.rotateBy(2 * Constants.STARTTING_SCREEN_POTION_ROTATE, Constants.STARTTING_SCREEN_POTION_ROTATE_DURATION),
                        Actions.rotateBy(-2 * Constants.STARTTING_SCREEN_POTION_ROTATE, Constants.STARTTING_SCREEN_POTION_ROTATE_DURATION)));
        SequenceAction potionSeq = Actions.sequence(repeat, Actions.run(new Runnable() {

            @Override
            public void run() {
                bombActions();
            }}));
        mPotionGreen.addAction(potionSeq);
        mStage.addActor(mPotionGreen);

        mPotionPurple = new Image(new Texture(Gdx.files.internal("misc/img_potion_purple.png")));
        mPotionPurple.setPosition(Constants.STAGE_WIDTH / 2 + 200, (Constants.STAGE_HEIGHT - mPotionPurple.getHeight()) / 2);
        mPotionPurple.setRotation(Constants.STARTTING_SCREEN_POTION_ROTATE);
        mPotionPurple.setOrigin(mPotionPurple.getWidth() / 2, mPotionPurple.getHeight() / 2);
        repeat = Actions.repeat(Constants.STARTTING_SCREEN_POTION_ROTATE_COUNT,
                Actions.sequence(Actions.rotateBy(-2 * Constants.STARTTING_SCREEN_POTION_ROTATE, Constants.STARTTING_SCREEN_POTION_ROTATE_DURATION),
                        Actions.rotateBy(2 * Constants.STARTTING_SCREEN_POTION_ROTATE, Constants.STARTTING_SCREEN_POTION_ROTATE_DURATION)));
        mPotionPurple.addAction(repeat);
        mStage.addActor(mPotionPurple);

        mSkip = new Image(new Texture(Gdx.files.internal("text/img_text_skip.png")));
        mSkip.setPosition(Constants.STAGE_WIDTH - mSkip.getWidth(), 5);
        RepeatAction skipRepeat = Actions.forever(Actions.sequence(Actions.alpha(0, 0.5f), Actions.alpha(1, 0.5f)));
        mSkip.addAction(skipRepeat);
        mStage.addActor(mSkip);

        mText_1 = new Image(new Texture(Gdx.files.internal("text/img_text_1.png")));
        mText_1.setPosition((Constants.STAGE_WIDTH - mText_1.getWidth()) / 2, Constants.STARTTING_SCREEN_TEXT_Y);
        mStage.addActor(mText_1);

        mText_2 = new Image(new Texture(Gdx.files.internal("text/img_text_2.png")));
        mText_2.setPosition((Constants.STAGE_WIDTH - mText_2.getWidth()) / 2, Constants.STARTTING_SCREEN_TEXT_Y);
        mText_3 = new Image(new Texture(Gdx.files.internal("text/img_text_3.png")));
        mText_3.setPosition((Constants.STAGE_WIDTH - mText_3.getWidth()) / 2, Constants.STARTTING_SCREEN_TEXT_Y);
        mText_4 = new Image(new Texture(Gdx.files.internal("text/img_text_4.png")));
        mText_4.setPosition((Constants.STAGE_WIDTH - mText_4.getWidth()) / 2,
                (Constants.STAGE_HEIGHT - mText_4.getHeight()) / 2);

        mBomb = new Image(new Texture(Gdx.files.internal("misc/img_bomb.png")));
        mBomb.setPosition((Constants.STAGE_WIDTH - mBomb.getWidth()) / 2, 0);

        mEffectBomb = Gdx.audio.newSound(Gdx.files.internal("sound/effect_bomb.mp3"));

        mSketch = new Image(new Texture(Gdx.files.internal("misc/img_hero_sketch.png")));
        mSketch.setPosition((Constants.STAGE_WIDTH - mSketch.getWidth()) / 2, (Constants.STAGE_HEIGHT - mSketch.getHeight()) / 2);
        mSketch.setOrigin(mSketch.getWidth() / 2, mSketch.getHeight() / 2);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    protected void bombActions() {
        mStage.getRoot().removeActor(mText_1);
        mStage.getRoot().removeActor(mPotionGreen);
        mStage.getRoot().removeActor(mPotionPurple);
        mStage.addActor(mBomb);
        mStage.addActor(mText_2);
        DelayAction delay = Actions.delay(2f, Actions.run(new Runnable() {

            @Override
            public void run() {
                heroFlyAwayActions();
            }}));
        mBomb.addAction(delay);

        mEffectBomb.play();
    }

    protected void heroFlyAwayActions() {
        mStage.getRoot().removeActor(mText_2);
        mStage.addActor(mText_3);

        mSketch.addAction(Actions.alpha(0));
        SequenceAction seq = Actions.sequence(Actions.alpha(1, Constants.STARTTING_SCREEN_FLY_AWAY_DURATION / 2),
                Actions.alpha(0, Constants.STARTTING_SCREEN_FLY_AWAY_DURATION / 2));
        ParallelAction parallel = Actions.parallel(seq, Actions.rotateBy(1200, Constants.STARTTING_SCREEN_FLY_AWAY_DURATION),
                Actions.scaleBy(4f, 4f, Constants.STARTTING_SCREEN_FLY_AWAY_DURATION),
                Actions.delay(Constants.STARTTING_SCREEN_FLY_AWAY_DURATION, Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        showFinalActions();
                    }})));
        mSketch.addAction(parallel);
        mStage.addActor(mSketch);
    }

    protected void showFinalActions() {
        mStage.getRoot().removeActor(mBomb);
        mStage.getRoot().removeActor(mText_3);
        mStage.addActor(mText_4);
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
        mStage.dispose();
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
        if (mMusicGlass != null & mMusicGlass.isPlaying()) {
            mMusicGlass.stop();
            mMusicGlass.dispose();
        }
        if (mEffectBomb!= null) {
            mEffectBomb.stop();
        }
        GameManager.getInstance().getGame().setScreen(new MissionSelectScreen(false));
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
