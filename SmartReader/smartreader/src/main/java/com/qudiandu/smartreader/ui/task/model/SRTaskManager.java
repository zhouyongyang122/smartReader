package com.qudiandu.smartreader.ui.task.model;

/**
 * Created by ZY on 17/7/24.
 */

public class SRTaskManager {

    private static SRTaskManager instance;

    int currentTaskClassId;

    private SRTaskManager() {

    }

    public static SRTaskManager getInstance() {
        if (instance == null) {
            instance = new SRTaskManager();
        }
        return instance;
    }

    public int getCurrentTaskClassId() {
        return currentTaskClassId;
    }

    public void setCurrentTaskClassId(int currentTaskClassId) {
        this.currentTaskClassId = currentTaskClassId;
    }
}
