package com.qudiandu.smartdub.ui.wordStudy.model.bean;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/12/11.
 */

public class SRWordStudyWord implements ZYIBaseBean {

    public int id;

    public String word;

    public String text_answer;

    public String pic_answer;

    public String meaning;

    public TextProblem text_problem;

    public PicProblem pic_problem;

    public class TextProblem implements ZYIBaseBean{
        public String A;
        public String B;
        public String C;
        public String D;
    }

    public class PicProblem implements ZYIBaseBean{
        public String A;
        public String B;
        public String C;
        public String D;
    }
}
