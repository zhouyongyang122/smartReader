package com.qudiandu.smartdub.base.event;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;
import com.qudiandu.smartdub.service.downNet.down.ZYIDownBase;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYEventDowloadUpdate implements ZYIBaseBean {

    public ZYIDownBase downloadEntity;

    public ZYEventDowloadUpdate(ZYIDownBase downloadEntity) {
        this.downloadEntity = downloadEntity;
    }
}
