package com.qudiandu.smartdub.base.activity.picturePicker;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

public class ZYPicture implements ZYIBaseBean {

    public int id;
    public String path;
    public boolean isSelected;

    public ZYPicture(int id, String path) {
        this.id = id;
        this.path = path;
    }
}
