package com.smartreader.ui.profile.model.bean;

import android.text.TextUtils;

import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.ui.login.model.bean.SRUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZY on 17/4/7.
 */

public class SRUserParams {

    public String nickname;

    public int sex;//1 男 2 女

    public String school;

    public String avatar;

    public String qianniuKey;

    public int grade;//年级 1-12

    public int age;

    public Map<String, String> getParamas() {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("nickname", nickname);
        paramas.put("sex", sex + "'");
        paramas.put("school", school);
        paramas.put("avatar", qianniuKey);
        paramas.put("grade", grade + "");
        paramas.put("age", age + "");
        return paramas;
    }

    public String checkParams() {
        if (TextUtils.isEmpty(qianniuKey)) {
            return "头像不能为空!";
        }
        if (TextUtils.isEmpty(nickname)) {
            return "昵称不能为空!";
        }
        if (TextUtils.isEmpty(school)) {
            return "学校不能为空!";
        }

        if (sex <= 0) {
            return "你还没有设置性别";
        }
        if (grade <= 0) {
            return "你还没有设置年级";
        }
        if (age <= 0) {
            return "你还没有设置年龄";
        }
        return null;
    }

    public static SRUserParams getDefUserParams() {
        SRUserParams userParams = new SRUserParams();
        SRUser user = SRUserManager.getInstance().getUser();
        userParams.nickname = user.nickname;
        userParams.sex = user.sex;
        userParams.school = user.school == null ? "还没有设置学校" : user.school;
        userParams.avatar = user.avatar;
        userParams.grade = user.grade;
        userParams.age = user.age;
        return userParams;
    }
}
