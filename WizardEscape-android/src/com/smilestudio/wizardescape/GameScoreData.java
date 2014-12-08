package com.smilestudio.wizardescape;

import cn.bmob.v3.BmobObject;

public class GameScoreData extends BmobObject {

    private static final long serialVersionUID = 3464910335477808429L;

    /**
     * Unique identifier for a user
     */
    private String uid;

    /**
     * such as 2-1-steps
     */
    private String mission;

    private int steps;

    /**
     * Send to Bmob service, used for ranking
     * 
     * @param uid Device id
     * @param missionLabel e.g. "2-1-steps"
     * @param steps Best steps for this mission
     */
    public GameScoreData(String deviceId, String missionLabel, int steps) {
        this.uid = deviceId;
        this.mission = missionLabel;
        this.steps = steps;
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getMissionLabel() {
        return mission;
    }

    public void setMissionLabel(String missionLabel) {
        mission = missionLabel;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

}
