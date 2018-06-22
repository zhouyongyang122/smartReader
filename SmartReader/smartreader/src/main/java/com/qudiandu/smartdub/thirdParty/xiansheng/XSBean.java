package com.qudiandu.smartdub.thirdParty.xiansheng;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.qudiandu.smartdub.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 17/6/14.
 */

public class XSBean implements ZYIBaseBean {

    public Result result;

    public int getScroeByChar(String value) {
        try {
            for (Detail detail : result.details) {
                if (detail.value_char.equals(value)) {
                    return detail.score;
                }
            }
        } catch (Exception e) {

        }
        return 60;
    }

    public class Result implements ZYIBaseBean {
        public int overall;//总分
        private int integrity;//完整度
        private int accuracy;//准确度
        private Fluency fluency;//流利度

        public int getIntegrity() {
            return integrity;
        }

        public int getAccuracy() {
            return accuracy;
        }

        public int getFluency() {
            return fluency == null ? 0 : fluency.overall;
        }

        public List<Detail> details;
    }

    public class Fluency implements ZYIBaseBean {
        public int overall;
    }

    public static class Detail implements ZYIBaseBean {

        public Detail(int score,String value_char){
            this.score = score;
            this.value_char = value_char;
        }

        public int score;

        @SerializedName("char")
        public String value_char;
    }

    public static XSBean createXSBean(String value) {
        XSBean bean = new Gson().fromJson(value, XSBean.class);
        return bean;
    }

}
