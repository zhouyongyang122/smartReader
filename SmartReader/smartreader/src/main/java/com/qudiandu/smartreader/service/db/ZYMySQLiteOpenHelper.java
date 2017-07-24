package com.qudiandu.smartreader.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.qudiandu.smartreader.service.db.entity.DaoMaster;
import com.qudiandu.smartreader.service.db.entity.SRBookDao;
import com.qudiandu.smartreader.service.db.entity.SRMarkBeanDao;
import com.qudiandu.smartreader.service.db.entity.SRUserDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ZY on 17/7/22.
 */

public class ZYMySQLiteOpenHelper extends DaoMaster.OpenHelper {

    public ZYMySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, SRBookDao.class, SRUserDao.class, SRMarkBeanDao.class);
    }
}
