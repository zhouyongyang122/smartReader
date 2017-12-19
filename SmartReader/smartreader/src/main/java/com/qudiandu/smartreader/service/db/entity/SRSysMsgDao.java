package com.qudiandu.smartreader.service.db.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.qudiandu.smartreader.ui.set.model.bean.SRSysMsg;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SRSYS_MSG".
*/
public class SRSysMsgDao extends AbstractDao<SRSysMsg, Integer> {

    public static final String TABLENAME = "SRSYS_MSG";

    /**
     * Properties of entity SRSysMsg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, int.class, "id", true, "ID");
        public final static Property IsRead = new Property(1, boolean.class, "isRead", false, "IS_READ");
    }


    public SRSysMsgDao(DaoConfig config) {
        super(config);
    }
    
    public SRSysMsgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SRSYS_MSG\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"IS_READ\" INTEGER NOT NULL );"); // 1: isRead
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SRSYS_MSG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SRSysMsg entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getIsRead() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SRSysMsg entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getIsRead() ? 1L: 0L);
    }

    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.getInt(offset + 0);
    }    

    @Override
    public SRSysMsg readEntity(Cursor cursor, int offset) {
        SRSysMsg entity = new SRSysMsg( //
            cursor.getInt(offset + 0), // id
            cursor.getShort(offset + 1) != 0 // isRead
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SRSysMsg entity, int offset) {
        entity.setId(cursor.getInt(offset + 0));
        entity.setIsRead(cursor.getShort(offset + 1) != 0);
     }
    
    @Override
    protected final Integer updateKeyAfterInsert(SRSysMsg entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public Integer getKey(SRSysMsg entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SRSysMsg entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
