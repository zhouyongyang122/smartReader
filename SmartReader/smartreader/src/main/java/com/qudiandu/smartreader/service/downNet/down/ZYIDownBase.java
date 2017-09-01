package com.qudiandu.smartreader.service.downNet.down;

/**
 * Created by ZY on 17/3/27.
 */

public interface ZYIDownBase {

    String getSavePath();

    void setSavePath(String savePath);

    long getTotal();

    void setTotal(long total);

    long getCurrent();

    void setCurrent(long current);

    int getStateValue();

    void setStateValue(int stateValue);

    ZYDownState getState();

    void setState(ZYDownState state);

    String getUrl();

    void setUrl(String url);

    String getId();

    long save();

    long update(boolean needInsert);

    void delete();
}
