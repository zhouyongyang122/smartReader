package com.qudiandu.smartreader.ui.myAudio.model;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.ui.main.model.bean.SRCatalogue;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCatalogueNew extends SRCatalogue implements ZYIBaseBean {

    //下面属性为服务器下发的属性(请求单元列表时)
    private int id;//服务器id

    private int uid;//用户id

    private int book_id;//对应的书本id

    private long create_time;//配音时间

    private String score;//分数

    private int views;//观看数

    private int supports;//点赞数

    //界面需要的属性
    private boolean isEdit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getScore() {
        try {
            return Integer.valueOf(score);
        } catch (Exception e) {

        }
        return 0;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getSupports() {
        return supports;
    }

    public void setSupports(int supports) {
        this.supports = supports;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
