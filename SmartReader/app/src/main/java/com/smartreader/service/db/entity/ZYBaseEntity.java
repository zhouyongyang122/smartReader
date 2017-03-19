package com.smartreader.service.db.entity;

import com.smartreader.service.db.ZYDBManager;

/**
 * Created by ZY on 17/3/17.
 */

public abstract class ZYBaseEntity{

    public abstract void save();

    public abstract void update();

    public abstract void delete();
}
