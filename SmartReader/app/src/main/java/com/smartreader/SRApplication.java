package com.smartreader;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.smartreader.common.utils.SRLog;
import com.smartreader.service.db.SRSqlite;
import com.smartreader.thirdParty.statistics.DataStatistics;

/**
 * Created by ZY on 17/3/13.
 */

public class SRApplication extends Application {

    public static SRApplication mInstance;

    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        //在主进程中进行初始化
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : activityManager.getRunningAppProcesses()) {
            if (info.pid == android.os.Process.myPid()) {
                if (getPackageName().equals(info.processName)) {
                    init();
                }
            }
        }
    }

    public static SRApplication getInstance(){
        return mInstance;
    }

    private void init(){
        mInstance = this;
         //日志初使化
        SRLog.init(BuildConfig.DEBUG);
        //数据统计
        DataStatistics.init(this);
        //数据库
        SRSqlite.getInstance().getInstance();
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
}
