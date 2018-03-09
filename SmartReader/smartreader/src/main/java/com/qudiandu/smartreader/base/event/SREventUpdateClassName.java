package com.qudiandu.smartreader.base.event;

import com.qudiandu.smartreader.ui.main.model.bean.SRClass;

/**
 * Created by ZY on 18/3/9.
 */

public class SREventUpdateClassName {

    public SRClass mClass;

    public SREventUpdateClassName(SRClass srClass){
        mClass = srClass;
    }
}
