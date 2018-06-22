package com.qudiandu.smartdub.ui.main.model.bean;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 17/3/27.
 */

public class SRHome implements ZYIBaseBean {

    public List<Adert> adert;

    public class Adert implements ZYIBaseBean {
        public String id;
        public String title;
        public String pic;
        public String url;
        public String type;
        public String content;
        public String share_pic;
        private int is_share;
    }
}
