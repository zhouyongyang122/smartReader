package com.qudiandu.smartdub.thirdParty.jpush;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/10/17.
 */

public class SRJpushMsg implements ZYIBaseBean {

    public static final String SYSTME_TYPE = "system";

    public static final String URL_TYPE = "go_url";

    public static final String OPEN_TYPE = "openapp";

    public String type;

    public Data data;

    public class Data implements ZYIBaseBean {
        public String url;
    }
}
