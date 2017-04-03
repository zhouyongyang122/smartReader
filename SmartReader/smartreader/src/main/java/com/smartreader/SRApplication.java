package com.smartreader;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.smartreader.service.db.ZYDBManager;
import com.smartreader.thirdParty.xunfei.XunFeiSDK;
import com.smartreader.utils.ZYLog;
import com.smartreader.thirdParty.statistics.DataStatistics;
import com.smartreader.utils.ZYUncaughtExceptionHandler;

import java.io.File;

/**
 * Created by ZY on 17/3/13.
 */

public class SRApplication extends Application implements ZYUncaughtExceptionHandler.OnUncaughtExceptionHappenListener {

    public static SRApplication mInstance;

    public static final String APP_ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "smartreader";

    public static final String BOOK_ROOT_DIR = APP_ROOT_DIR + File.separator + "course" + File.separator;

    public static final String BOOK_ZIP_ROOT_DIR = APP_ROOT_DIR + File.separator + "courseZip" + File.separator;

    public static final String TRACT_AUDIO_ROOT_DIR = APP_ROOT_DIR + File.separator + "tractAudio" + File.separator;

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

    public static SRApplication getInstance() {
        return mInstance;
    }

    private void init() {
        mInstance = this;
        //日志初使化
        ZYLog.init(BuildConfig.DEBUG);
        //数据统计
        DataStatistics.init(this);
        //数据库
        ZYDBManager.getInstance();
        initFileDir();

        ZYUncaughtExceptionHandler crashHandler = ZYUncaughtExceptionHandler.getInstance();
        crashHandler.init(this, APP_ROOT_DIR, BuildConfig.DEBUG);
        crashHandler.setListener(this);

        XunFeiSDK.getInstance();
    }

    private void initFileDir() {
        File file = new File(BOOK_ROOT_DIR);
        if (!file.exists()) {
            ZYLog.e(getClass().getSimpleName(), "initFileDir: " + file.mkdirs() + file.getAbsolutePath());
        }

        file = new File(BOOK_ZIP_ROOT_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(TRACT_AUDIO_ROOT_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Override
    public void onUncaughtExceptionHappen(Thread thread, Throwable ex) {

    }
}
