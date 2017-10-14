package com.qudiandu.smartreader.base.event;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/9/9.
 */

public class ZYAudionPlayEvent implements ZYIBaseBean {

    //播放状态 参见FZAudioPlaySevice
    public int state;

    //播放的当前时长
    public int currentDuration;

    //音频时长
    public int totalDuration;

    String msg;

    public String url;

    public ZYAudionPlayEvent(int state, String msg, int currentDuration, int totalDuration) {
        this.currentDuration = currentDuration;
        this.totalDuration = totalDuration;
        this.msg = msg;
        this.state = state;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
