package com.smartreader;

import android.content.SharedPreferences;

/**
 * Created by ZY on 17/3/27.
 */

public class ZYPreferenceHelper {

    private static ZYPreferenceHelper instance;

    public static final String DEF_PRE_NAME = "def_pre_name";

    public static final String TIME_OFFSET = "TimeOffset";

    public static final String INSERT_DEF_BOOK = "insert_def_book";

    private SharedPreferences defPre;

    private ZYPreferenceHelper() {

    }

    public static ZYPreferenceHelper getInstance() {
        if (instance == null) {
            synchronized (ZYPreferenceHelper.class) {
                if (instance == null) {
                    instance = new ZYPreferenceHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 设置已经插入默认的书籍
     *
     * @param isInserted
     */
    public void setInsertDefBook(boolean isInserted) {
        getDefPre().edit().putBoolean(INSERT_DEF_BOOK, isInserted).apply();
    }

    /**
     * 是否已经插入默认的书籍
     *
     * @return
     */
    public boolean isInsertDefBook() {
        return getDefPre().getBoolean(INSERT_DEF_BOOK, false);
    }

    /**
     * 设置请求验证的时间搓
     *
     * @param timeOffset
     */
    public void setTimeOffset(long timeOffset) {
        getDefPre().edit().putLong(TIME_OFFSET, timeOffset).commit();
    }


    /**
     * 返回请求验证的时间搓
     *
     * @return
     */
    public long getTimeOffset() {
        return getDefPre().getLong(TIME_OFFSET, 0);
    }

    public SharedPreferences getDefPre() {
        if (defPre == null) {
            defPre = SRApplication.getInstance().getSharedPreferences(DEF_PRE_NAME, 0);
        }
        return defPre;
    }
}
