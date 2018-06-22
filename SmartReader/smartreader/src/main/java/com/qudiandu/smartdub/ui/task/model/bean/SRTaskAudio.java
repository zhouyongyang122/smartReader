package com.qudiandu.smartdub.ui.task.model.bean;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/9/4.
 */

public class SRTaskAudio implements ZYIBaseBean {

    public long audioSize;

    public String audioPath;

    public SRTaskAudio(long audioSize, String audioPath) {
        this.audioSize = audioSize;
        this.audioPath = audioPath;
    }
}
