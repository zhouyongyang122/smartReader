package com.qudiandu.smartreader.ui.main.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.utils.ZYDateUtils;

import java.util.List;

/**
 * Created by ZY on 17/7/22.
 */

public class SRTask implements ZYIBaseBean {

    public int task_id;

    public int group_id;

    public int book_id;

    //课本名称
    public String name;

    //课本图片地址
    public String pic;

    //课本下载地址
    public String zip;

    public int catalogue_id;

    public int cur_num;

    public String create_time;

    public int limit_num;

    public String unit;//单元标题

    public String title;//小节标题

    public String page_url;//封面图

    public List<Finish> finish;

    public boolean isEdit;

    public String getCreateTime() {
        try {
            return ZYDateUtils.getTimeString(Long.parseLong(create_time) * 1000, ZYDateUtils.MMDD12);
        } catch (Exception e) {

        }
        return "";
    }

    public class Finish {
        public int show_id;
        public int uid;
        public int group_id;
        public int book_id;
        public int catalogue_id;
        public int task_id;
        public String comment;
        public String create_time;
        public int score;
        public int views;
        public int supports;
    }

    public SRBook getBook() {
        SRBook book = new SRBook();
        book.book_id = book_id + "";
        book.pic = pic;
        book.zip = zip;
        book.name = name;
        return book;
    }
}
