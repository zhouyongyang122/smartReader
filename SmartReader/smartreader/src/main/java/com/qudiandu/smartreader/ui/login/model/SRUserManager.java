package com.qudiandu.smartreader.ui.login.model;

import android.text.TextUtils;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.service.db.ZYDBManager;
import com.qudiandu.smartreader.service.db.entity.SRUserDao;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;

import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public class SRUserManager {

    private static SRUserManager instance;

    private SRUser user;

    private SRUserManager() {
        user = getLoginUser();
    }

    public static SRUserManager getInstance() {
        if (instance == null) {
            instance = new SRUserManager();
        }
        return instance;
    }

    private SRUser getLoginUser() {
        SRUserDao userDao = ZYDBManager.getInstance().getReadableDaoSession().getSRUserDao();
        List<SRUser> users = userDao.loadAll();
        if (users == null || users.size() < 0) {
            return new SRUser();
        } else {
            for (SRUser user : users) {
                if (user.isLoginUser) {
                    return user;
                }
            }
        }
        return new SRUser();
    }

    public SRUser getUser() {
        return user;
    }

    public void loginOut() {
        user.isLoginUser = false;
        user.update();
        user = new SRUser();
    }

    public void setUser(SRUser user) {
        if (user != null) {
            this.user = user;
            this.user.isLoginUser = true;
            this.user.update();
        }
    }

    public boolean isGuesterUser(boolean needIntentToLogin) {
        boolean isGuester = TextUtils.isEmpty(user.uid);
        if (needIntentToLogin && isGuester) {
            //跳到登录
            SRApplication.getInstance().getCurrentActivity().startActivity(SRLoginActivity.createIntent(SRApplication.getInstance().getCurrentActivity()));
        }
        return isGuester;
    }
}