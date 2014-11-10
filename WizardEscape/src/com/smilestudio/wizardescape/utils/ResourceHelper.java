package com.smilestudio.wizardescape.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ResourceHelper {

    public static Image getBgImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("background/img_wildfield.png")));
            case 2:
                return new Image(new Texture(Gdx.files.internal("background/img_desert.png")));
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("background/img_flowering_cherry.png")));
        }
    }

    public static Image getGridImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("background/img_wild_background_grid.png")));
            case 2:
                return new Image(new Texture(Gdx.files.internal("background/img_desert_background_grid.png")));
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("background/img_cherry_background_grid.png")));
        }
    }

    public static Image getObstacleImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("misc/img_obstacle_tree_root.png")));
            case 2:
                return new Image(new Texture(Gdx.files.internal("misc/img_obstacle_skeleton.png")));
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("misc/img_obstacle_stone.png")));
        }
    }

    public static Image getMovableImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("misc/img_movable_barrel.png")));
            case 2:
                return new Image(new Texture(Gdx.files.internal("misc/img_movable_panda.png")));
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("misc/img_movable_light.png")));
        }
    }

    public static Image getCircleImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("misc/img_bg_circle_mission_1_relief.png")));
            case 2:
                return new Image(new Texture(Gdx.files.internal("misc/img_bg_circle_mission_2_relief.png")));
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("misc/img_bg_circle_mission_3_relief.png")));
        }
    }

    public static Music getBkMusic(int mission) {
        switch (mission) {
            case 1:
                return Gdx.audio.newMusic(Gdx.files.internal("sound/music_bk_mission_1.mp3"));
            case 2:
                return Gdx.audio.newMusic(Gdx.files.internal("sound/music_bk_mission_2.mp3"));
            case 3:;
            default:
                return Gdx.audio.newMusic(Gdx.files.internal("sound/music_bk_mission_3.mp3"));
        }
    }

    public static TextureRegion[] getHeroDownRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_front_5.png")));
        return regions;
    }

    public static TextureRegion[] getHeroUpRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_back_5.png")));
        return regions;
    }

    public static TextureRegion[] getHeroLeftRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_left_5.png")));
        return regions;
    }

    public static TextureRegion[] getHeroRightRegions() {
        TextureRegion[] regions = new TextureRegion[6];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_run_right_5.png")));
        return regions;
    }

    public static TextureRegion[] getHeroStandRegions() {
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

    public static TextureRegion[] getDogRightRegions() {
        TextureRegion[] regions = new TextureRegion[8];
        regions[0] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_0.png")));
        regions[1] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_1.png")));
        regions[2] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_2.png")));
        regions[3] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_3.png")));
        regions[4] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_4.png")));
        regions[5] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_5.png")));
        regions[6] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_6.png")));
        regions[7] = new TextureRegion(new Texture(Gdx.files.internal("character/img_dog_walk_7.png")));
        return regions;
    }

    public static TextureRegion[] getDogLeftRegions() {
        TextureRegion[] regions = new TextureRegion[8];
        Texture texture = new Texture(Gdx.files.internal("character/img_dog_walk_0.png"));
        regions[0] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        texture = new Texture(Gdx.files.internal("character/img_dog_walk_1.png"));
        regions[1] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        texture = new Texture(Gdx.files.internal("character/img_dog_walk_2.png"));
        regions[2] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        texture = new Texture(Gdx.files.internal("character/img_dog_walk_3.png"));
        regions[3] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        texture = new Texture(Gdx.files.internal("character/img_dog_walk_4.png"));
        regions[4] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        texture = new Texture(Gdx.files.internal("character/img_dog_walk_5.png"));
        regions[5] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        texture = new Texture(Gdx.files.internal("character/img_dog_walk_6.png"));
        regions[6] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        texture = new Texture(Gdx.files.internal("character/img_dog_walk_7.png"));
        regions[7] = new TextureRegion(texture, texture.getWidth(), 0, -texture.getWidth(), texture.getHeight());
        return regions;
    }
}
