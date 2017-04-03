package com.smartreader.ui.me;

import com.smartreader.service.db.ZYDBManager;
import com.smartreader.service.db.entity.SRUserDao;
import com.smartreader.service.db.entity.ZYBaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ZY on 17/4/2.
 */

@Entity
public class SRUser extends ZYBaseEntity {

    @Id
    public String uid;

    public String nickname;

    public String avatar;

    public int sex;//1 男 2 女

    public String school;

    public String refresh_token;

    public int endtime;

    public String upload_token;

    public String picture_token;

    public int grade;//年级 1-12

    public String auth_token;

    public boolean isLoginUser;//是否是当前登录的用户

    public int type;//0:手机号码登录 1:qq登录 2:微博登录 3:微信登陆

    @Generated(hash = 1834124643)
    public SRUser(String uid, String nickname, String avatar, int sex,
                  String school, String refresh_token, int endtime, String upload_token,
                  String picture_token, int grade, String auth_token, boolean isLoginUser,
                  int type) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.sex = sex;
        this.school = school;
        this.refresh_token = refresh_token;
        this.endtime = endtime;
        this.upload_token = upload_token;
        this.picture_token = picture_token;
        this.grade = grade;
        this.auth_token = auth_token;
        this.isLoginUser = isLoginUser;
        this.type = type;
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
}
