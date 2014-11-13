package com.smilestudio.wizardescape;

public interface AnalyticsListener {

    public void startMission (String mission);
    public void failMission (String mission);
    public void finishMission (String mission);
    public void use(String mission, int steps);
}
