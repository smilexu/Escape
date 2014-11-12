package com.smilestudio.wizardescape.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;
import com.smilestudio.wizardescape.actors.HeroActor;
import com.smilestudio.wizardescape.utils.Constants;
import com.smilestudio.wizardescape.utils.ResourceHelper;

public class CoverScreen implements Screen, InputProcessor {

    private Stage mStage;
    private Image mStartButton;
    private Game mGame;
    private Sound mEffectDog;

    public CoverScreen(Game game) {
        mGame = game;
        GameManager.getInstance().setGame(game);
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
        loadSounds();
        initResource();

        Gdx.input.setInputProcessor(this);
    }

    private void loadSounds() {
        mEffectDog = Gdx.audio.newSound(Gdx.files.internal("sound/effect_dog.wav"));
    }

    private void releaseAudio() {
        mEffectDog.dispose();
    }
    private void initResource() {
        Texture logoTexture = new Texture(Gdx.files.internal("character/img_fmls.png"));
        TextureRegion[][] tmpRegions = TextureRegion.split(logoTexture, logoTexture.getWidth() / 2, logoTexture.getHeight() / 2);
        TextureRegion[] logoRegions = new TextureRegion[4];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                logoRegions[index] = tmpRegions[i][j];
                index++;
            }
        }

        mStage = new Stage(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT, false);

        Image bkImg = new Image(new Texture(Gdx.files.internal("background/img_cover_background.png")));
        bkImg.setPosition(0, 0);
        mStage.addActor(bkImg);

        float duration = Constants.ANIMATION_HERO_ACTION_DURATION;
        HeroActor hero = new HeroActor(ResourceHelper.getHeroUpRegions(), duration, ResourceHelper.getHeroDownRegions(), duration,
                ResourceHelper.getHeroLeftRegions(), duration, ResourceHelper.getHeroRightRegions(), duration,
                ResourceHelper.getHeroStandRegions(), duration);
        hero.setPosition(540, 280);
        hero.setScale(1.5f);
        mStage.addActor(hero);

        final HeroActor dog = new HeroActor(null, duration, null, duration, ResourceHelper.getDogLeftRegions(), duration,
                ResourceHelper.getDogRightRegions(), duration, null, duration);
        dog.setPosition(200, 185);
        RunnableAction turnRight = Actions.run(new Runnable() {

            @Override
            public void run() {
                dog.setStatus(HeroActor.STATUS_RIGHT);
                mEffectDog.play();
            }});
        RunnableAction turnLeft = Actions.run(new Runnable() {

            @Override
            public void run() {
                dog.setStatus(HeroActor.STATUS_LEFT);
            }});
        SequenceAction dogWalk = Actions.sequence(Actions.moveBy(400, 0, 5f), turnLeft, Actions.moveBy(-400, 0, 5f), turnRight);
        RepeatAction repeat = Actions.forever(dogWalk);
        dog.addAction(repeat);
        dog.setStatus(HeroActor.STATUS_RIGHT);
        mEffectDog.play();
        mStage.addActor(dog);

        Texture startTexture = new Texture(Gdx.files.internal("buttons/img_start.png"));
        int width = startTexture.getWidth();
        int height = startTexture.getHeight();
        mStartButton = new Image(startTexture);
        mStartButton.setSize(width, height);
        mStartButton.setPosition(Constants.COVER_SCREEN_POSITION_X_START, Constants.COVER_SCREEN_POSITION_Y_START);

        SizeToAction sizeUpAction = Actions.sizeTo(width * 1.1f, height * 1.1f, 0.8f);
        SizeToAction sizeDownAction = Actions.sizeTo(width, height, 0.8f);
        SequenceAction sequence = Actions.sequence(sizeUpAction, sizeDownAction);
        RepeatAction foreverAction = Actions.forever(sequence);
        mStartButton.addAction(foreverAction);
        mStage.addActor(mStartButton);
    }

    @Override
    public void hide() {
        releaseAudio();
    }

    @Override
    public void pause() {
        releaseAudio();
    }

    @Override
    public void resume() {
        loadSounds();
    }

    @Override
    public void dispose() {
        mStage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Keys.BACK == keycode) {
            releaseAudio();
            Gdx.app.exit();
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
        Vector2 stagePoint = mStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        if (mStage.hit(stagePoint.x, stagePoint.y, false) == mStartButton) {
            releaseAudio();
            mGame.setScreen(new MissionSelectScreen());
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
