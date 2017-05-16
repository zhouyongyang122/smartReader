package com.qudiandu.smartreader.ui.main.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 17/3/28.
 */

public class SRPage implements ZYIBaseBean {

    private int page_id;

    private String page_name;

    private String localRootDirPath;

    private String picPath;

    private List<SRTract> track;

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public String getPage_name() {
        return page_name;
    }

    public void setPage_name(String page_name) {
        this.page_name = page_name;
    }

    public List<SRTract> getTrack() {
        return track;
    }

    public void setTrack(List<SRTract> track) {
        this.track = track;
    }

    public String getPicPath() {
        return localRootDirPath + "bookpage/" + page_name;
    }

    public void setLocalRootDirPath(String localRootDirPath) {
        this.localRootDirPath = localRootDirPath;
    }

    public String getLocalRootDirPath() {
        return localRootDirPath;
    }
}
