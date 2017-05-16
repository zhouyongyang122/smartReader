package com.qudiandu.smartreader.thirdParty.translate;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by zhouyongyang on 17/1/18.
 */

public class YouDaoBean implements ZYIBaseBean {

    public int errorCode;

    //单词
    public String query;

    public YouDaoBasic basic;

    /**
     * 音标
     *
     * @return
     */
    public String getPhonetic() {
        if (basic != null && basic.phonetic != null) {
            return basic.phonetic;
        }
        return "";
    }

    /**
     * 词意
     *
     * @return
     */
    public String getExplains() {
        if (basic != null && basic.explains != null && basic.explains.size() > 0) {
            StringBuffer meaning = new StringBuffer();
            for (String value : basic.explains) {
                meaning.append(value + "\n");
            }
            return meaning.toString();
        }
        return "";
    }

    public class YouDaoBasic implements ZYIBaseBean {
        //音标
        public String phonetic;
        //词意
        public List<String> explains;
    }
}
