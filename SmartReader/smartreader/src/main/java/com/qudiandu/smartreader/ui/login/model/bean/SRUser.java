package com.qudiandu.smartreader.ui.login.model.bean;

import android.text.TextUtils;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.service.db.ZYDBManager;
import com.qudiandu.smartreader.service.db.entity.SRUserDao;
import com.qudiandu.smartreader.service.db.entity.ZYBaseEntity;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.vip.activity.SRVipActivity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by ZY on 17/4/2.
 */

@Entity
public class SRUser extends ZYBaseEntity {

    public static final int DEF_TYPE = 0;

    public static final int STUDY_TYPE = 1;

    public static final int TEACHER_TYPE = 2;

    @Id
    public String uid;

    public String nickname;

    public String avatar;

    public int sex;//1 男 2 女

    public String school;

    public String refresh_token;

    public int age;

    public int endtime;

    public String upload_token;

    public String picture_token;

    public int grade;//年级 1-12

    public String auth_token;

    public boolean isLoginUser;//是否是当前登录的用户

    public int type;//0:手机号码登录 1:qq登录 2:微博登录 3:微信登陆

    public int user_type;//用户身份 1 学生 2 老师  0待选择

    public String mobile;

    public int school_id;//机构id

    public String is_vip;

    public String vip_endtime;

    @Transient
    public boolean isCheck;

    @Generated(hash = 906363200)
    public SRUser(String uid, String nickname, String avatar, int sex, String school, String refresh_token,
                  int age, int endtime, String upload_token, String picture_token, int grade, String auth_token,
                  boolean isLoginUser, int type, int user_type, String mobile, int school_id, String is_vip,
                  String vip_endtime) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.sex = sex;
        this.school = school;
        this.refresh_token = refresh_token;
        this.age = age;
        this.endtime = endtime;
        this.upload_token = upload_token;
        this.picture_token = picture_token;
        this.grade = grade;
        this.auth_token = auth_token;
        this.isLoginUser = isLoginUser;
        this.type = type;
        this.user_type = user_type;
        this.mobile = mobile;
        this.school_id = school_id;
        this.is_vip = is_vip;
        this.vip_endtime = vip_endtime;
    }

    @Generated(hash = 628471356)
    public SRUser() {
    }

    @Override
    public long save() {
        SRUserDao userDao = ZYDBManager.getInstance().getWritableDaoSession().getSRUserDao();
        return userDao.insertOrReplace(this);
    }

    @Override
    public long update() {
        SRUserDao userDao = ZYDBManager.getInstance().getWritableDaoSession().getSRUserDao();
        return userDao.insertOrReplace(this);
    }

    @Override
    public void delete() {
        SRUserDao userDao = ZYDBManager.getInstance().getWritableDaoSession().getSRUserDao();
        userDao.delete(this);
    }

    public boolean isTeacher() {
        return user_type == SRUser.TEACHER_TYPE;
    }

    public boolean isStudent() {
        return user_type == SRUser.STUDY_TYPE;
    }

    public boolean isNoIdentity() {
        return user_type == SRUser.DEF_TYPE;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getEndtime() {
        return this.endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public String getUpload_token() {
        return this.upload_token;
    }

    public void setUpload_token(String upload_token) {
        this.upload_token = upload_token;
    }

    public String getPicture_token() {
        return this.picture_token;
    }

    public void setPicture_token(String picture_token) {
        this.picture_token = picture_token;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAuth_token() {
        return this.auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public boolean getIsLoginUser() {
        return this.isLoginUser;
    }

    public void setIsLoginUser(boolean isLoginUser) {
        this.isLoginUser = isLoginUser;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLoginUser() {
        return isLoginUser;
    }

    public void setLoginUser(boolean loginUser) {
        isLoginUser = loginUser;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isVip() {
        try {
            if (Integer.parseInt(is_vip) >= 1) {
                long time = System.currentTimeMillis() / 1000L + ZYPreferenceHelper.getInstance().getTimeOffset();
                if (!TextUtils.isEmpty(vip_endtime)) {
                    return Long.parseLong(vip_endtime) >= time;
                }
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean isVip(boolean intentToLogin) {
        if (!isVip()) {
            if (intentToLogin) {
                SRApplication.getInstance().showVipBuy();
            }
            return false;
        }
        return true;
    }

    public boolean isYearVip() {
        try {
            return isVip() && (Integer.parseInt(is_vip) == 2);
        } catch (Exception e) {

        }
        return false;
    }

    public int getUser_type() {
        return this.user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getSchool_id() {
        return this.school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public String getIs_vip() {
        return this.is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public String getVip_endtime() {
        return this.vip_endtime;
    }

    public void setVip_endtime(String vip_endtime) {
        this.vip_endtime = vip_endtime;
    }
}
