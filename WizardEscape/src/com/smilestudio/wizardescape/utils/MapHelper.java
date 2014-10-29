package com.smilestudio.wizardescape.utils;

import com.badlogic.gdx.math.Vector2;
import com.smilestudio.wizardescape.GameManager;

public class MapHelper {

    public static Vector2 getPostionByCell(int x, int y) {
        Vector2 position = new Vector2();
        int positionY = Constants.OFFSET_Y + Constants.CELL_SIZE_HEIGHT * (GameManager.ROW - 1);
        position.set(Constants.OFFSET_X + x * Constants.CELL_SIZE_WIDTH, positionY - y * Constants.CELL_SIZE_HEIGHT);
        return position;
    }

}
