package com.qudiandu.smartreader.base.activity.picturePicker;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

import java.util.ArrayList;

public class ZYAlbum implements ZYIBaseBean{

    public String id;
    public String coverPath;
    public String name;
    public long   dateAdded;
    public ArrayList<ZYPicture> pictures = new ArrayList<>();
    public boolean isSelected;
}
