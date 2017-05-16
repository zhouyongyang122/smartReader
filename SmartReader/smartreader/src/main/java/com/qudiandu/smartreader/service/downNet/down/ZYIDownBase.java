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

    int getConnectonTime();

    void setConnectonTime(int connectonTime);

    int getStateValue();

    void setStateValue(int stateValue);

    ZYDownState getState();

    void setState(ZYDownState state);

    String getUrl();

    void setUrl(String url);

    ZYDownloadScriberListener getListener();

    void setListener(ZYDownloadScriberListener listener);

    ZYDownloadService getDownloadService();

    void setDownloadService(ZYDownloadService downloadService);

    String getId();

    long save();

    long update();

    void delete();
}
