package com.smilestudio.wizardescape.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
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

}
