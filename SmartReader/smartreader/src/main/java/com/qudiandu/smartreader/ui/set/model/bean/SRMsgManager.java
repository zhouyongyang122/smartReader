package com.qudiandu.smartreader.ui.set.model.bean;

import android.content.SharedPreferences;

import com.qudiandu.smartreader.SRApplication;

/**
 * Created by ZY on 17/7/26.
 */

public class SRMsgManager {

    static SRMsgManager instance;

    SharedPreferences sharedPreferences;

    private SRMsgManager() {
        sharedPreferences = SRApplication.getInstance().getSharedPreferences("SRMsgManager", 0);
    }

    public static SRMsgManager getInstance() {
        if (instance == null) {
            instance = new SRMsgManager();
        }
        return instance;
    }

    public boolean hasMsgRemind() {
        return sharedPreferences.getBoolean("hasRemind", false);
    }

    public void saveMsgRemind(int lastMsgId) {
        int savedId = sharedPreferences.getInt("msgId", 0);
        if (savedId != lastMsgId) {
            sharedPreferences.edit().putInt("msgId", lastMsgId).putBoolean("hasRemind", true).commit();
        }
    }

    public void clearMsgRemind() {
        sharedPreferences.edit().putBoolean("hasRemind", false).commit();
    }
}
