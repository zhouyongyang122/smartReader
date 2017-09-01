package com.qudiandu.smartreader.ui.main.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/7/23.
 */

public class SRTaskTitle implements ZYIBaseBean {

    public String title;

    public boolean isEdit;

    public SRTaskTitle(String title) {
        this.title = title;
    }

    public SRTaskTitle(String title, boolean isEdit) {
        this.title = title;
        isEdit = isEdit;
    }
}
