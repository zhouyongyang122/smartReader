package com.smartreader.thirdParty.sina;

import android.app.Activity;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.smartreader.service.net.ZYOkHttpNetManager;
import com.smartreader.ui.SRAppConstants;
import com.smartreader.ui.login.model.bean.SRThridLoginParamas;
import com.smartreader.utils.ZYLog;
import com.smartreader.utils.ZYMd5Utils;
import com.third.loginshare.entity.WeiboUserInfo;

/**
 * Created by ZY on 17/4/14.
 */

public class SinaManager {

    private static SinaManager instance;

    private SsoHandler mLoginHandler;

    private AuthInfo mLoginauthInfo;

    private SinaManager() {

    }

    public static SinaManager getInstance() {
        if (instance == null) {
            instance = new SinaManager();
        }
        return instance;
    }

    public void initLogin(Activity activity) {
        try {
            mLoginauthInfo = new AuthInfo(activity.getApplicationContext(),
                    SRAppConstants.SINA_APP_KEY, SRAppConstants.SINA_REDIRECT_URL,
                    SRAppConstants.SINA_SCOPE);
            mLoginHandler = new SsoHandler(activity, mLoginauthInfo);
        } catch (Exception e) {
            mLoginHandler = null;
        }
    }

    public void login(Activity activity, final SinaLoginListener loginListener) {
        mLoginHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle arg0) {
                try {
                    if (arg0 == null) {
                        if (loginListener != null) {
                            loginListener.onCancel("微博登录失败,请重新尝试!");
                        }
                        return;
                    }
                    final SRThridLoginParamas loginParamas = new SRThridLoginParamas();
                    loginParamas.setType(SRThridLoginParamas.TYPE_SINA);

                    final String openId = arg0.getString("uid");
                    final String authToken = arg0.getString("access_token");
                    String auth_url = "https://api.weibo.com/2/users/show.json?uid="
                            + openId + "&access_token=" + authToken;
                    loginParamas.setAuth_url(auth_url);

                    String url = "https://api.weibo.com/2/users/show.json?access_token=" + authToken + "&uid=" + openId;

                    ZYOkHttpNetManager.getInstance().requestGet(WeiboUserInfo.class, url, new ZYOkHttpNetManager.OkHttpNetListener<WeiboUserInfo>() {
                        @Override
                        public void onFailure(String message) {
                            if (loginListener != null) {
                                loginListener.onError("微博登录失败,请重新尝试!");
                            }
                        }

                        @Override
                        public void onSuccess(WeiboUserInfo userInfo) {
                            try {
                                loginParamas.setToken(ZYMd5Utils.getMD5Str("funpeiyin" + openId));
                                loginParamas.setNickname(userInfo.screen_name);
                                loginParamas.setAvatar(userInfo.profile_image_url);
                                loginParamas.setSex(userInfo.gender.equalsIgnoreCase("m") ? 1 : 2);
                                if (loginListener != null) {
                                    loginListener.onSuccess(loginParamas);
                                }
                            } catch (Exception e) {
                                ZYLog.e(SinaManager.class.getSimpleName(), "login-erro: " + e.getMessage());
                                if (loginListener != null) {
                                    loginListener.onError("微博登录失败,请重新尝试!");
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    ZYLog.e(SinaManager.class.getSimpleName(), "login-erro: " + e.getMessage());
                    if (loginListener != null) {
                        loginListener.onError("微博登录失败,请重新尝试!");
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ZYLog.e(SinaManager.class.getSimpleName(), "login-erro: " + e.getMessage());
                if (loginListener != null) {
                    loginListener.onError("微博登录失败,请重新尝试!");
                }
            }

            @Override
            public void onCancel() {
                if (loginListener != null) {
                    loginListener.onCancel("微博登录取消!");
                }
            }
        });
    }

    public SsoHandler getLoginHandler() {
        return mLoginHandler;
    }

    public AuthInfo getLoginauthInfo() {
        return mLoginauthInfo;
    }

    public interface SinaLoginListener {
        void onCancel(String msg);

        void onError(String msg);

        void onSuccess(SRThridLoginParamas loginParamas);
    }
}
