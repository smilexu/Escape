package com.smilestudio.wizardescape.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.smilestudio.wizardescape.GameManager;

public class MapHelper {

    public static Vector2 getPostionByCell(int x, int y) {
        Vector2 position = new Vector2();
        int positionY = Constants.OFFSET_Y + Constants.CELL_SIZE_HEIGHT * (GameManager.ROW - 1);
        position.set(Constants.OFFSET_X + x * Constants.CELL_SIZE_WIDTH, positionY - y * Constants.CELL_SIZE_HEIGHT);
        return position;
    }

    public static Image getBgImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("background/img_wildfield.png")));
            case 2:
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("background/img_desert.png")));
        }
    }

    public static Image getGridImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("background/img_wild_background_grid.png")));
            case 2:
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("background/img_desert_background_grid.png")));
        }
    }

    public static Image getObstacleImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("misc/img_obstacle_tree_root.png")));
            case 2:
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("misc/img_obstacle_skeleton.png")));

        }
    }

    public static Image getMovableImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("misc/img_movable_barrel.png")));
            case 2:
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("misc/img_movable_panda.png")));
        }
    }

    public static Image getCircleImage(int mission) {
        switch (mission) {
            case 1:
                return new Image(new Texture(Gdx.files.internal("misc/img_bg_circle_mission_1_relief.png")));
            case 2:
            case 3:
            default:
                return new Image(new Texture(Gdx.files.internal("misc/img_bg_circle_mission_2_relief.png")));
        }
    }

}
