package com.qudiandu.smartreader.ui.rank.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRank implements ZYIBaseBean {

    public int id;

    public int uid;

    public int book_id;

    public int catalogue_id;

    public String avatar;

    public String name;

    public String unit;

    public String title;

    public String page_url;

    public int score;

    public int views;

    public int supports;

    public int is_vip;

    public int is_support;

    public boolean isVip() {
        return is_vip >= 1;
    }

    public boolean isSupport() {
        return is_support >= 1;
    }
}
