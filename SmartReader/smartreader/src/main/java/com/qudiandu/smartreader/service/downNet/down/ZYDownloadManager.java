package com.qudiandu.smartreader.service.downNet.down;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.utils.ZYLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYDownloadManager {

    private static ZYDownloadManager instance;

    ZYDownloadService downloadService;

    public static ZYDownloadManager getInstance() {
        if (instance == null) {
            synchronized (ZYDownloadManager.class) {
                if (instance == null) {
                    instance = new ZYDownloadManager();
                }
            }
        }
        return instance;
    }

    public boolean addBook(ZYIDownBase downBase) {
        return downloadService.addBook(downBase);
    }

    public void addBooks(List<ZYIDownBase> downBases) {
        downloadService.addBooks(downBases);
    }

    public void delBook(ZYIDownBase downBase) {
        downloadService.delBook(downBase);
    }


    public boolean cancleBook(String id) {
        return downloadService.cancleBook(id);
    }

    public boolean cancleAll() {
        return downloadService.cancleAll();
    }

    public void removeTask(String id) {
        downloadService.removeTask(id);
    }

    public void startSer() {
        try {
            Intent intent = new Intent();
            intent.setClass(SRApplication.getInstance(), ZYDownloadService.class);
            SRApplication.getInstance().startService(intent);
            SRApplication.getInstance().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {

        }
    }

    public void stopSer() {
        try {
            Intent intent = new Intent();
            intent.setClass(SRApplication.getInstance(), ZYDownloadService.class);
            SRApplication.getInstance().stopService(intent);
            SRApplication.getInstance().unbindService(conn);
        } catch (Exception e) {

        }
    }

    //使用ServiceConnection来监听Service状态的变化
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            downloadService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //这里我们实例化audioService,通过binder来实现
            ZYLog.e(ServiceConnection.class.getSimpleName(), "onServiceConnected");
            downloadService = ((ZYDownloadService.DownloadBinder) binder).getService();

        }
    };
}
