package com.smilestudio.wizardescape;

public interface GameListener {

    public static final int TYPE_MAGIC_ITEM = 1;
    public static final int TYPE_KEY = 2;
    public static final int TYPE_TELEPORT = 3;
    public static final int TYPE_PORTAL = 4;
    public static final int TYPE_CHEERS = 5;
    public static final int TYPE_DOG = 6;
    public static final int TYPE_TELEPORT_UNVAILABLE = 7;

    void onGameSuccess();
    void onSoundPlay(int type);
}
