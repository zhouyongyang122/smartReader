package com.smartreader.service.downNet.down;

/**
 * Created by ZY on 17/3/17.
 */

public enum ZYDownState {
    WAIT(-1),
    START(0),
    DOWNING(1),
    PAUSE(2),
    STOP(3),
    ERROR(4),
    FINISH(5);
    private int state = -1;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    ZYDownState(int state) {
        this.state = state;
    }
}
