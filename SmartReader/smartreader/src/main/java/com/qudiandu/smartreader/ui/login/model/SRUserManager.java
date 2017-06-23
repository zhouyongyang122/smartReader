package com.qudiandu.smartreader.ui.login.model;

import android.text.TextUtils;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.service.db.ZYDBManager;
import com.qudiandu.smartreader.service.db.entity.SRUserDao;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;

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

    public static void refreshToken() {
        if (!TextUtils.isEmpty(SRUserManager.getInstance().getUser().getRefresh_token())) {
            ZYNetSubscription.subscription(new SRLoginModel().refreshToken(SRUserManager.getInstance().getUser().getRefresh_token()), new ZYNetSubscriber<ZYResponse<SRUser>>() {
                @Override
                public void onSuccess(ZYResponse<SRUser> response) {
                    super.onSuccess(response);
                    if (response != null) {
                        SRUser user = SRUserManager.getInstance().getUser();
                        user.auth_token = response.data.auth_token;
                        user.picture_token = response.data.picture_token;
                        user.upload_token = response.data.upload_token;
                        SRUserManager.getInstance().setUser(user);
                    } else {
                        gotoReLogin();
                    }
                }

                @Override
                public void onFail(String message) {
//                    super.onFail(message);
                    gotoReLogin();
                }
            });
        } else {
            gotoReLogin();
        }
    }

    public static void gotoReLogin() {
        try {
            ZYToast.show(SRApplication.getInstance(), "登录信息失效,请重新登录");
            SRApplication.getInstance().getCurrentActivity().startActivity(SRLoginActivity.createIntent(SRApplication.getInstance().getCurrentActivity()));
        } catch (Exception e) {
            ZYLog.e("SRUserManager", "refreshToken:" + e.getMessage());
        }
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
