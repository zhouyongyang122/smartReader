package com.smartreader.thirdParty.statistics;

import android.content.Context;

import com.smartreader.BuildConfig;
import com.smartreader.base.utils.SRChannelUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ZY on 17/3/15.
 */

public class DataStatistics {

    public final static String U_MENT_KEY = "558b820e67e58e3d17000eee";

    public static void init(Context context){

        //友盟统计
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(context,U_MENT_KEY, SRChannelUtil.getChannel(context),MobclickAgent.EScenarioType.E_UM_NORMAL));
    }

    public static void onResume(Context context){
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context){
        MobclickAgent.onPause(context);
    }
}
