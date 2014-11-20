package com.smilestudio.wizardescape.utils;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenshotFactory {

    public static final String SCREENSHOT_FILE_NAME = "we_screenshot.png";

    public static String saveScreenshot(){
        String path = Gdx.files.getExternalStoragePath() + SCREENSHOT_FILE_NAME;
        try{
            FileHandle fh = new FileHandle(path);
            Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
        } catch (Exception e){
            return null;
        }
        return path;
    }

    private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

        if (yDown) {
            // Flip the pixmap upside down
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        }

        Pixmap pixmapOut = new Pixmap(640, 360, Format.RGBA8888);
        pixmapOut.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, 640, 360);
        return pixmapOut;
    }
}
