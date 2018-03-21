package com.qudiandu.smartreader.ui.vip.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 18/3/1.
 */

public class SRVip implements ZYIBaseBean {

    public String is_vip;

    public String vip_endtime;

    public String[] pay_type;

    public List<Price> package_list;

    public class Price implements ZYIBaseBean{

        public int id;

        public String amount;

        public int days;

        public String desc;

        public int choose;

        public boolean isSelected;
    }

    public static class Rights implements ZYIBaseBean{

        public int res;

        public String title;

        public String url;

        public boolean isSelected;
    }

}
