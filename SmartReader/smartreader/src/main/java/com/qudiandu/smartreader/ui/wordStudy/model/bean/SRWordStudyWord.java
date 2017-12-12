package com.qudiandu.smartreader.ui.wordStudy.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

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

    public class TextProblem {
        public String A;
        public String B;
        public String C;
        public String D;
    }

    public class PicProblem {
        public String A;
        public String B;
        public String C;
        public String D;
    }
}
