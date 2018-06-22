package com.qudiandu.smartdub;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.qudiandu.smartdub.service.db.ZYDBManager;
import com.qudiandu.smartdub.service.downNet.down.ZYDownloadManager;
import com.qudiandu.smartdub.thirdParty.jpush.SRJPushSDK;
import com.qudiandu.smartdub.ui.SRAppConstants;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.main.model.SRIJKPlayManager;
import com.qudiandu.smartdub.ui.main.model.bean.SRBook;
import com.qudiandu.smartdub.ui.vip.activity.SRVipActivity;
import com.qudiandu.smartdub.utils.ZYLog;
import com.qudiandu.smartdub.thirdParty.statistics.DataStatistics;
import com.qudiandu.smartdub.utils.ZYUncaughtExceptionHandler;

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

    public static final String TASK_AUDIO_ROOT_DIR = APP_ROOT_DIR + File.separator + "taskAudio" + File.separator;

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
                    SRIJKPlayManager.getInstance();
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

//        SRUserManager.refreshToken();

        ZYDownloadManager.getInstance().startSer();

        SRBook.changeErrorStatus();

        SRJPushSDK.init(this);
    }

    private void initFileDir() {
        File file = new File(BOOK_ROOT_DIR);
        if (!file.exists()) {
            ZYLog.e(getClass().getSimpleName(), "initFileDir: " + file.mkdirs() + file.getAbsolutePath());
            file.mkdirs();
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

        file = new File(TASK_AUDIO_ROOT_DIR);
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

    public void showVipBuy() {
        new AlertDialog.Builder(currentActivity).setTitle("会员特权").setMessage("购买趣点读会员即可免费查看所以教材内容,是否购买?")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentActivity.finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("去购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!SRUserManager.getInstance().gotoVip()) {
                            currentActivity.startActivity(SRVipActivity.createIntent(currentActivity));
                        }
                        dialog.dismiss();
                    }
                }).setCancelable(false).create().show();
    }

    @Override
    public void onUncaughtExceptionHappen(Thread thread, Throwable ex) {

    }
}
