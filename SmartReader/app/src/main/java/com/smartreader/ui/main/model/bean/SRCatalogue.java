package com.smartreader.ui.main.model.bean;

import com.smartreader.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/3/28.
 */

public class SRCatalogue implements ZYIBaseBean {

    private int catalogue_id;

    private String unit;

    private double duration;

    private String title;

    private int page;

    private String mp3name;

    private String mp3url;

    private String mp3url_hiq;

    private int clickread;

    private String page_id;

    public int getCatalogue_id() {
        return catalogue_id;
    }

    public void setCatalogue_id(int catalogue_id) {
        this.catalogue_id = catalogue_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getMp3name() {
        return mp3name;
    }

    public void setMp3name(String mp3name) {
        this.mp3name = mp3name;
    }

    public String getMp3url() {
        return mp3url;
    }

    public void setMp3url(String mp3url) {
        this.mp3url = mp3url;
    }

    public String getMp3url_hiq() {
        return mp3url_hiq;
    }

    public void setMp3url_hiq(String mp3url_hiq) {
        this.mp3url_hiq = mp3url_hiq;
    }

    public int getClickread() {
        return clickread;
    }

    public void setClickread(int clickread) {
        this.clickread = clickread;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }
}
