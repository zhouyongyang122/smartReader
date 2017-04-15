package com.third.loginshare;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.third.loginshare.entity.WeChatUserInfo;
import com.third.loginshare.net.HttpUtil;
import com.third.loginshare.net.JsonUtil;
import com.third.loginshare.net.ThirdPartyServer;

public class GetWeChatUserInfoTask extends AsyncTask<Void, Void, WeChatUserInfo>
{
    private String accessToken;
    private String openId;
    
    public GetWeChatUserInfoTask(String accessToken, String openId)
    {
        this.accessToken = accessToken;
        this.openId = openId;
    }
    
    @Override
    protected WeChatUserInfo doInBackground(Void... voids)
    {
        try
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("access_token", accessToken));
            params.add(new BasicNameValuePair("openid", openId));
            
            String json =
                HttpUtil.getInstace()
                    .httpGetRequestJson(ThirdPartyServer.getUrl(ThirdPartyServer.URL_WECHAT_GET_USER_INFO, params));
            return JsonUtil.gson.fromJson(json, new TypeToken<WeChatUserInfo>()
            {
            }.getType());
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
}
