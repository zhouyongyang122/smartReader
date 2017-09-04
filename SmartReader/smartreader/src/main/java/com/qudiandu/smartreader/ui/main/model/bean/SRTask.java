package com.qudiandu.smartreader.ui.main.model.bean;

import android.text.TextUtils;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.service.downNet.down.ZYIDownBase;
import com.qudiandu.smartreader.utils.ZYDateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/7/22.
 */

public class SRTask implements ZYIBaseBean {

    public static final int TASK_TYPE_RECORD = 1;//配音任务

    public static final int TASK_TYPE_AUDIO = 2;//语音任务

    public static final int TASK_TYPE_PIC = 3;//选图任务

    public static final int TASK_TYPE_LISTEN = 4;//课程录音任务

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

    public ArrayList<Finish> finish;

    public boolean isEdit;

    //1.4.0
    public int ctype;//任务类型
    public String audio;//课堂录音地址

    public String getCreateTime() {
        try {
            return ZYDateUtils.getTimeString(Long.parseLong(create_time) * 1000, ZYDateUtils.MMDD12);
        } catch (Exception e) {

        }
        return "";
    }


    public boolean isFinished(){
        return finish != null && finish.size() > 0;
    }

    public boolean hasComment(){
        if(isFinished()){
            return !TextUtils.isEmpty(finish.get(0).comment);
        }
        return false;
    }

    public class Finish implements ZYIBaseBean {
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
        public String answer;
        public String audio;
        public String problem_id;
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
