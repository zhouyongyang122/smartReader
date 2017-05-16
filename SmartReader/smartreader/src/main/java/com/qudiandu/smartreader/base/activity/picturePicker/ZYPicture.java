package com.qudiandu.smartreader.base.activity.picturePicker;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

public class ZYPicture implements ZYIBaseBean {

    public int id;
    public String path;
    public boolean isSelected;

    public ZYPicture(int id, String path) {
        this.id = id;
        this.path = path;
    }
}
