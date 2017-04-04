package com.smartreader.ui.login.model;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.base.mvp.ZYBaseModel;
import com.smartreader.ui.login.model.bean.SRUser;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/4/4.
 */

public class SRLoginModel extends ZYBaseModel {

    //0 普通发送 1 注册 2 找回密码
    public static final int CODE_DEF_TYPE = 0;
    public static final int CODE_REGISTER_TYPE = 1;
    public static final int CODE_FIND_PAW_TYPE = 2;

    // 1 qq登录 2 微博登录 3 微信登陆
    public static final int LOGIN_QQ_TYPE = 1;
    public static final int LOGIN_WB_TYPE = 2;
    public static final int LOGIN_WX__TYPE = 3;

    public Observable<ZYResponse<SRUser>> login(String mobile, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("password", password);
        return mApi.login(params);
    }

    public Observable<ZYResponse<SRUser>> thirdLogin(String token, String auth_url, int type, String nickname, String avatar, int sex, String signature) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("auth_url", auth_url);
        params.put("type", type + "");
        //type 1 qq登录 2 微博登录 3 微信登陆
        params.put("nickname", nickname);
        params.put("avatar", avatar);
        params.put("sex", sex + "");
        params.put("signature", signature);
        return mApi.thirdLogin(params);
    }

    public Observable<ZYResponse<SRUser>> register(String mobile, String password, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("code", code);
        return mApi.register(params);
    }

    public Observable<ZYResponse<SRUser>> bindMobile(String mobile, String password, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("code", code);
        return mApi.bindMobile(params);
    }

    public Observable<ZYResponse<SRUser>> resetPassword(String mobile, String password, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("code", code);
        return mApi.resetPassword(params);
    }

    public Observable<ZYResponse<SRUser>> loginOut() {
        return mApi.loginOut();
    }

    public Observable<ZYResponse<SRUser>> editUser(Map<String, String> params) {
        return mApi.editUser(params);
    }

    public Observable<ZYResponse<SRUser>> changePassword(String password, String newpassword) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("newpassword", newpassword);
        params.put("password", password);
        return mApi.changePassword(params);
    }

    public Observable<ZYResponse> mobileCode(String mobile, int type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("type", type + "");
        //type 0 普通发送 1 注册 2 找回密码
        return mApi.mobileCode(params);
    }

    public Observable<ZYResponse<SRUser>> refreshToken(String refresh_token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("refresh_token", refresh_token);
        return mApi.refreshToken(params);
    }
}
