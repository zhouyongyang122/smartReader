package com.qudiandu.smartreader.ui.main.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.utils.ZYLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

    private int clickread;

    private String page_id;

    public String page_url;//对应的书本封面地址

    private ArrayList<String> pageIds = new ArrayList<String>();

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

    public int getClickread() {
        return clickread;
    }

    public void setClickread(int clickread) {
        this.clickread = clickread;
    }

    public boolean containsPage(String pageId) {
        if (pageIds.size() <= 0) {
            try {
                pageIds.addAll(Arrays.asList(page_id.split(",")));
            } catch (Exception e) {

            }
        }
        return pageIds.contains(pageId);
    }

    public int getFristPageId() {
        try {
            if (pageIds.size() <= 0) {
                try {
                    pageIds.addAll(Arrays.asList(page_id.split(",")));
                } catch (Exception e) {
                    ZYLog.e(getClass().getSimpleName(),"getFristPageId-error: " + e.getMessage());
                }
            }
            return pageIds.size() > 0 ? Integer.valueOf(pageIds.get(0)) : 0;
        }catch (Exception e){
            ZYLog.e(getClass().getSimpleName(),"getFristPageId-error: " + e.getMessage());
        }
        return 0;
    }

    public String getPage_url() {
        return page_url;
    }

    public void setPage_url(String page_url) {
        this.page_url = page_url;
    }
}
