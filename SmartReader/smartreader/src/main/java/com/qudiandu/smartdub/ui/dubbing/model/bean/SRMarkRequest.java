package com.qudiandu.smartdub.ui.dubbing.model.bean;

import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkRequest implements ZYIBaseBean {

    public String book_id;

    public String page_id;

    public String track_id;

    public String score;

    public Map<String, String> getParamas() {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("book_id", book_id);
        paramas.put("page_id", page_id);
        paramas.put("track_id", track_id);
        paramas.put("score", score);
        return paramas;
    }
}
