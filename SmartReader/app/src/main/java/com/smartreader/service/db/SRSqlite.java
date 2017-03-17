package com.smartreader.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.smartreader.SRApplication;
import com.smartreader.common.utils.SRLog;

/**
 * Created by ZY on 17/3/15.
 */

public class SRSqlite extends OrmLiteSqliteOpenHelper {

    private static SRSqlite DB;

    public static String DATABASE_NAME = "smartReaderApp.db";

    public static int DATABASE_VERSION = 1;

    public SRSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SRSqlite getInstance() {
        if (DB == null) {
            DB = OpenHelperManager.getHelper(SRApplication.getInstance().getApplicationContext(), SRSqlite.class);
        }
        return DB;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
//            TableUtils.createTableIfNotExists(connectionSource, FZUser.class);
            SRLog.e(getClass().getSimpleName(),"onCreate-success");
        } catch (Exception e) {
            SRLog.e(getClass().getSimpleName(),"onCreate-error: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            onCreate(database, connectionSource);
            if (oldVersion < 3) {
//                database.execSQL("alter table fzusermodel add column is_vip integer");
            }
            SRLog.e(getClass().getSimpleName(), "onUpgrade-success......");
        } catch (Exception e) {
            SRLog.e(getClass().getSimpleName(), "onUpgrade-error: " + e.getMessage());
        }
    }
}
