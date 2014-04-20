package com.smilestudio.wizardescape.utils;

import com.badlogic.gdx.math.Vector2;

public class MapHelper {

    public static Vector2 getPostionByCell(int x, int y) {
        Vector2 position = new Vector2();
        position.set(Constants.OFFSET_X + x * Constants.CELL_SIZE, Constants.OFFSET_Y + y * Constants.CELL_SIZE);
        return position;
    }

}
