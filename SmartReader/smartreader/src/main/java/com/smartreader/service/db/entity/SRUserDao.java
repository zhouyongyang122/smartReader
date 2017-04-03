package com.smartreader.service.db.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.smartreader.ui.me.SRUser;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SRUSER".
*/
public class SRUserDao extends AbstractDao<SRUser, String> {

    public static final String TABLENAME = "SRUSER";

    /**
     * Properties of entity SRUser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Uid = new Property(0, String.class, "uid", true, "UID");
        public final static Property Nickname = new Property(1, String.class, "nickname", false, "NICKNAME");
        public final static Property Avatar = new Property(2, String.class, "avatar", false, "AVATAR");
        public final static Property Sex = new Property(3, int.class, "sex", false, "SEX");
        public final static Property School = new Property(4, String.class, "school", false, "SCHOOL");
        public final static Property Refresh_token = new Property(5, String.class, "refresh_token", false, "REFRESH_TOKEN");
        public final static Property Endtime = new Property(6, int.class, "endtime", false, "ENDTIME");
        public final static Property Upload_token = new Property(7, String.class, "upload_token", false, "UPLOAD_TOKEN");
        public final static Property Picture_token = new Property(8, String.class, "picture_token", false, "PICTURE_TOKEN");
        public final static Property Grade = new Property(9, int.class, "grade", false, "GRADE");
        public final static Property Auth_token = new Property(10, String.class, "auth_token", false, "AUTH_TOKEN");
        public final static Property IsLoginUser = new Property(11, boolean.class, "isLoginUser", false, "IS_LOGIN_USER");
        public final static Property Type = new Property(12, int.class, "type", false, "TYPE");
    }


    public SRUserDao(DaoConfig config) {
        super(config);
    }
    
    public SRUserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SRUSER\" (" + //
                "\"UID\" TEXT PRIMARY KEY NOT NULL ," + // 0: uid
                "\"NICKNAME\" TEXT," + // 1: nickname
                "\"AVATAR\" TEXT," + // 2: avatar
                "\"SEX\" INTEGER NOT NULL ," + // 3: sex
                "\"SCHOOL\" TEXT," + // 4: school
                "\"REFRESH_TOKEN\" TEXT," + // 5: refresh_token
                "\"ENDTIME\" INTEGER NOT NULL ," + // 6: endtime
                "\"UPLOAD_TOKEN\" TEXT," + // 7: upload_token
                "\"PICTURE_TOKEN\" TEXT," + // 8: picture_token
                "\"GRADE\" INTEGER NOT NULL ," + // 9: grade
                "\"AUTH_TOKEN\" TEXT," + // 10: auth_token
                "\"IS_LOGIN_USER\" INTEGER NOT NULL ," + // 11: isLoginUser
                "\"TYPE\" INTEGER NOT NULL );"); // 12: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SRUSER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SRUser entity) {
        stmt.clearBindings();
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(1, uid);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(2, nickname);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(3, avatar);
        }
        stmt.bindLong(4, entity.getSex());
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(5, school);
        }
 
        String refresh_token = entity.getRefresh_token();
        if (refresh_token != null) {
            stmt.bindString(6, refresh_token);
        }
        stmt.bindLong(7, entity.getEndtime());
 
        String upload_token = entity.getUpload_token();
        if (upload_token != null) {
            stmt.bindString(8, upload_token);
        }
 
        String picture_token = entity.getPicture_token();
        if (picture_token != null) {
            stmt.bindString(9, picture_token);
        }
        stmt.bindLong(10, entity.getGrade());
 
        String auth_token = entity.getAuth_token();
        if (auth_token != null) {
            stmt.bindString(11, auth_token);
        }
        stmt.bindLong(12, entity.getIsLoginUser() ? 1L: 0L);
        stmt.bindLong(13, entity.getType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SRUser entity) {
        stmt.clearBindings();
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(1, uid);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(2, nickname);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(3, avatar);
        }
        stmt.bindLong(4, entity.getSex());
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(5, school);
        }
 
        String refresh_token = entity.getRefresh_token();
        if (refresh_token != null) {
            stmt.bindString(6, refresh_token);
        }
        stmt.bindLong(7, entity.getEndtime());
 
        String upload_token = entity.getUpload_token();
        if (upload_token != null) {
            stmt.bindString(8, upload_token);
        }
 
        String picture_token = entity.getPicture_token();
        if (picture_token != null) {
            stmt.bindString(9, picture_token);
        }
        stmt.bindLong(10, entity.getGrade());
 
        String auth_token = entity.getAuth_token();
        if (auth_token != null) {
            stmt.bindString(11, auth_token);
        }
        stmt.bindLong(12, entity.getIsLoginUser() ? 1L: 0L);
        stmt.bindLong(13, entity.getType());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SRUser readEntity(Cursor cursor, int offset) {
        SRUser entity = new SRUser( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // uid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nickname
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // avatar
            cursor.getInt(offset + 3), // sex
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // school
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // refresh_token
            cursor.getInt(offset + 6), // endtime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // upload_token
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // picture_token
            cursor.getInt(offset + 9), // grade
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // auth_token
            cursor.getShort(offset + 11) != 0, // isLoginUser
            cursor.getInt(offset + 12) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SRUser entity, int offset) {
        entity.setUid(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setNickname(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAvatar(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSex(cursor.getInt(offset + 3));
        entity.setSchool(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRefresh_token(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEndtime(cursor.getInt(offset + 6));
        entity.setUpload_token(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPicture_token(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGrade(cursor.getInt(offset + 9));
        entity.setAuth_token(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setIsLoginUser(cursor.getShort(offset + 11) != 0);
        entity.setType(cursor.getInt(offset + 12));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SRUser entity, long rowId) {
        return entity.getUid();
    }
    
    @Override
    public String getKey(SRUser entity) {
        if(entity != null) {
            return entity.getUid();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SRUser entity) {
        return entity.getUid() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}