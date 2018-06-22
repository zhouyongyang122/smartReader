package com.qudiandu.smartdub.ui.set.model.bean;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ZY on 17/4/9.
 */

@Entity
public class SRSysMsg implements ZYIBaseBean {

    @Id
    public int id;

    public boolean isRead;

    @Transient
    public String title;

    @Transient
    public String content;

    @Transient
    public long create_time;

    @Transient
    public String pic;

    @Transient
    public String url;

    @Generated(hash = 1354916027)
    public SRSysMsg(int id, boolean isRead) {
        this.id = id;
        this.isRead = isRead;
    }

    @Generated(hash = 1367722386)
    public SRSysMsg() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
