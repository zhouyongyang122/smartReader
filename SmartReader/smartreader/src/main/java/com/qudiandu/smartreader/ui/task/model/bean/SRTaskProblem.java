package com.qudiandu.smartreader.ui.task.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.utils.ZYDateUtils;

import java.util.List;

/**
 * Created by ZY on 17/9/3.
 */

public class SRTaskProblem implements ZYIBaseBean {

    public int task_id;

    public String title;

    public String page_url;

    public int group_id;

    public int uid;

    public String nickname;

    public String avatar;

    public String problem_id;

    public int ctype;

    public long create_time;

    public List<Problem> problems;

    public String user_answer;

    private Teacher teacher;

    public Teacher getTeacher() {
        if (teacher == null) {
            teacher = new Teacher();
            teacher.avatar = avatar;
            teacher.uid = uid;
            teacher.nickname = nickname;
            teacher.create_time = create_time;
        }
        return teacher;
    }

    public class Problem implements ZYIBaseBean {
        public int problem_id;

        public String title;

        public String description;

        public String pic;

        public String audio;

        public float audio_timelen;

        public String answer;

        public int ctype;

        public AnswerPic answer_pic;
    }

    public class AnswerPic implements ZYIBaseBean {
        public String A;
        public String B;
        public String C;
        public String D;
    }

    public class Teacher {

        public int uid;

        public String nickname;

        public String avatar;

        private long create_time;

        public String getCreateTime() {
            try {
                return "发布于 " + ZYDateUtils.getTimeString(create_time, ZYDateUtils.YYMMDDHHMMSS24);
            } catch (Exception e) {

            }
            return "";
        }

    }


}
