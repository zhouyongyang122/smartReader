package com.qudiandu.smartreader.base.event;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.service.downNet.down.ZYIDownBase;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYEventDowloadUpdate implements ZYIBaseBean {

    public ZYIDownBase downloadEntity;

    public ZYEventDowloadUpdate(ZYIDownBase downloadEntity) {
        this.downloadEntity = downloadEntity;
    }
}
