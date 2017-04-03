package com.smartreader.ui.me.model;

import com.smartreader.service.db.ZYDBManager;
import com.smartreader.service.db.entity.SRUserDao;
import com.smartreader.ui.me.SRUser;

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

    public boolean isGuesterUser(boolean needIntentToLogin) {
        if (needIntentToLogin) {
            //跳到登录
        }
        return user.uid != null;
    }
}
