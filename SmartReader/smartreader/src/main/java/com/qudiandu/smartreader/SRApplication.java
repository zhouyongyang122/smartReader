package com.qudiandu.smartreader;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.qudiandu.smartreader.BuildConfig;
import com.qudiandu.smartreader.service.db.ZYDBManager;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadManager;
import com.qudiandu.smartreader.thirdParty.xiansheng.XianShengSDK;
import com.qudiandu.smartreader.thirdParty.xunfei.XunFeiSDK;
import com.qudiandu.smartreader.ui.SRAppConstants;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.thirdParty.statistics.DataStatistics;
import com.qudiandu.smartreader.utils.ZYUncaughtExceptionHandler;

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

    public static final String CACHE_ROOT_DIR = APP_ROOT_DIR + File.separator + "cache" + File.separator;

    public static final String MERGE_AUDIO_ROOT_DIR = APP_ROOT_DIR + File.separator + "mergeAudio" + File.separator;

    public static final String MY_AUDIO_ROOT_DIR = APP_ROOT_DIR + File.separator + "myAudio" + File.separator;

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

        //在这里初始化
        initBugTags();

        SRUserManager.refreshToken();

        ZYDownloadManager.getInstance().startSer();

        SRBook.changeErrorStatus();
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

        file = new File(CACHE_ROOT_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(MERGE_AUDIO_ROOT_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(MY_AUDIO_ROOT_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void initBugTags() {
        if (BuildConfig.DEBUG) {
            BugtagsOptions options = new BugtagsOptions.Builder()
                    .trackingLocation(true)
                    .trackingCrashLog(true).build();
            Bugtags.start(SRAppConstants.BUGTAGS_KEY, this, Bugtags.BTGInvocationEventShake, options);
        } else {
            Bugtags.start(SRAppConstants.BUGTAGS_KEY, this, Bugtags.BTGInvocationEventNone);
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
