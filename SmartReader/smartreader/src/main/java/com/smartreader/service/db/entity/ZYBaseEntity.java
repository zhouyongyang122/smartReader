package com.smartreader.service.db.entity;

import com.smartreader.base.bean.ZYIBaseBean;
import com.smartreader.service.db.ZYDBManager;

/**
 * Created by ZY on 17/3/17.
 */

public abstract class ZYBaseEntity implements ZYIBaseBean {

    public abstract long save();

    public abstract long update();

    public abstract void delete();
}
