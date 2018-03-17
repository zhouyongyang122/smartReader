package com.qudiandu.smartreader;

import android.content.SharedPreferences;

import com.qudiandu.smartreader.ui.login.model.SRUserManager;

/**
 * Created by ZY on 17/3/27.
 */

public class ZYPreferenceHelper {

    private static ZYPreferenceHelper instance;

    public static final String DEF_PRE_NAME = "def_pre_name";

    public static final String TIME_OFFSET = "TimeOffset";

    public static final String INSERT_DEF_BOOK = "insert_def_book";

    public static final String SHOW_TRACT_BG = "show_tract_bg";//是否显示句子的点击区域

    public static final String SHOW_TRACT_TRANS = "show_tract_trans";//是否显示句子的翻译

    public static final String TRACT_SPEED = "tract_speed";//句子朗读速度

    public static final String JPUSH_UPLOADED = "jpush_uploaded";//极光id是否上传

    public static final String DUB_GUIDE = "dub_guide";//配音引导

    public static final String SCHOOL_ID = "school_id";//当前选择的班级对应的学校id

    public static final String IDENTITY_COMFIRM = "indentity_confirm";//身份确认

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

    public String getSchoolId(){
        return getDefPre().getString(SCHOOL_ID,"");
    }

    public void saveSchoolId(String schoolId){
        getDefPre().edit().putString(SCHOOL_ID,schoolId).commit();
    }

    public boolean hasIdentityComfirmed() {
        String uid = SRUserManager.getInstance().getUser().getUid();
        String key = uid + "_" + IDENTITY_COMFIRM;
        return getDefPre().getBoolean(key, false);
    }

    public void identityComfirm() {
        String uid = SRUserManager.getInstance().getUser().getUid();
        String key = uid + "_" + IDENTITY_COMFIRM;
        getDefPre().edit().putBoolean(key, true).commit();
    }

    public boolean hasUploadJPushId() {
        String uid = SRUserManager.getInstance().getUser().getUid();
        String key = uid + "_" + JPUSH_UPLOADED;
        return getDefPre().getBoolean(key, false);
    }

    public void setUploadJPushId(boolean uploaded) {
        String uid = SRUserManager.getInstance().getUser().getUid();
        String key = uid + "_" + JPUSH_UPLOADED;
        getDefPre().edit().putBoolean(key, uploaded).commit();
    }

    public boolean hasDubGuide() {
        String uid = SRUserManager.getInstance().getUser().getUid();
        String key = uid + "_" + DUB_GUIDE;
        return getDefPre().getBoolean(key, false);
    }

    public void setDubGuide(boolean guided) {
        String uid = SRUserManager.getInstance().getUser().getUid();
        String key = uid + "_" + DUB_GUIDE;
        getDefPre().edit().putBoolean(key, guided).commit();
    }

    public int getSelectBookId(int classId) {
        return getDefPre().getInt("select_" + classId, 0);
    }

    public void saveSelectBookId(int classId, int bookId) {
        getDefPre().edit().putInt("select_" + classId, bookId).commit();
    }

    public void setShowTractBg(boolean showTractBg) {
        getDefPre().edit().putBoolean(SHOW_TRACT_BG, showTractBg).commit();
    }

    public boolean isShowTractBg() {
        return getDefPre().getBoolean(SHOW_TRACT_BG, false);
    }

    public void setShowTractTrans(boolean showTractTrans) {
        getDefPre().edit().putBoolean(SHOW_TRACT_TRANS, showTractTrans).commit();
    }

    public boolean isShowTractTrans() {
        return getDefPre().getBoolean(SHOW_TRACT_TRANS, false);
    }

    public void setTractSpeed(int tractSpeed) {
        getDefPre().edit().putInt(TRACT_SPEED, tractSpeed).commit();
    }

    public int getTractSpeed() {
        return getDefPre().getInt(TRACT_SPEED, 50);
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
