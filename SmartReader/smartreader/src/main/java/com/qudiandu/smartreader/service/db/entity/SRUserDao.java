package com.qudiandu.smartreader.service.db.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.qudiandu.smartreader.ui.login.model.bean.SRUser;

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
        public final static Property Age = new Property(6, int.class, "age", false, "AGE");
        public final static Property Endtime = new Property(7, int.class, "endtime", false, "ENDTIME");
        public final static Property Upload_token = new Property(8, String.class, "upload_token", false, "UPLOAD_TOKEN");
        public final static Property Picture_token = new Property(9, String.class, "picture_token", false, "PICTURE_TOKEN");
        public final static Property Grade = new Property(10, int.class, "grade", false, "GRADE");
        public final static Property Auth_token = new Property(11, String.class, "auth_token", false, "AUTH_TOKEN");
        public final static Property IsLoginUser = new Property(12, boolean.class, "isLoginUser", false, "IS_LOGIN_USER");
        public final static Property Type = new Property(13, int.class, "type", false, "TYPE");
        public final static Property User_type = new Property(14, int.class, "user_type", false, "USER_TYPE");
        public final static Property Mobile = new Property(15, String.class, "mobile", false, "MOBILE");
        public final static Property School_id = new Property(16, int.class, "school_id", false, "SCHOOL_ID");
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
                "\"AGE\" INTEGER NOT NULL ," + // 6: age
                "\"ENDTIME\" INTEGER NOT NULL ," + // 7: endtime
                "\"UPLOAD_TOKEN\" TEXT," + // 8: upload_token
                "\"PICTURE_TOKEN\" TEXT," + // 9: picture_token
                "\"GRADE\" INTEGER NOT NULL ," + // 10: grade
                "\"AUTH_TOKEN\" TEXT," + // 11: auth_token
                "\"IS_LOGIN_USER\" INTEGER NOT NULL ," + // 12: isLoginUser
                "\"TYPE\" INTEGER NOT NULL ," + // 13: type
                "\"USER_TYPE\" INTEGER NOT NULL ," + // 14: user_type
                "\"MOBILE\" TEXT," + // 15: mobile
                "\"SCHOOL_ID\" INTEGER NOT NULL );"); // 16: school_id
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
        stmt.bindLong(7, entity.getAge());
        stmt.bindLong(8, entity.getEndtime());
 
        String upload_token = entity.getUpload_token();
        if (upload_token != null) {
            stmt.bindString(9, upload_token);
        }
 
        String picture_token = entity.getPicture_token();
        if (picture_token != null) {
            stmt.bindString(10, picture_token);
        }
        stmt.bindLong(11, entity.getGrade());
 
        String auth_token = entity.getAuth_token();
        if (auth_token != null) {
            stmt.bindString(12, auth_token);
        }
        stmt.bindLong(13, entity.getIsLoginUser() ? 1L: 0L);
        stmt.bindLong(14, entity.getType());
        stmt.bindLong(15, entity.getUser_type());
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(16, mobile);
        }
        stmt.bindLong(17, entity.getSchool_id());
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
        stmt.bindLong(7, entity.getAge());
        stmt.bindLong(8, entity.getEndtime());
 
        String upload_token = entity.getUpload_token();
        if (upload_token != null) {
            stmt.bindString(9, upload_token);
        }
 
        String picture_token = entity.getPicture_token();
        if (picture_token != null) {
            stmt.bindString(10, picture_token);
        }
        stmt.bindLong(11, entity.getGrade());
 
        String auth_token = entity.getAuth_token();
        if (auth_token != null) {
            stmt.bindString(12, auth_token);
        }
        stmt.bindLong(13, entity.getIsLoginUser() ? 1L: 0L);
        stmt.bindLong(14, entity.getType());
        stmt.bindLong(15, entity.getUser_type());
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(16, mobile);
        }
        stmt.bindLong(17, entity.getSchool_id());
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
            cursor.getInt(offset + 6), // age
            cursor.getInt(offset + 7), // endtime
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // upload_token
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // picture_token
            cursor.getInt(offset + 10), // grade
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // auth_token
            cursor.getShort(offset + 12) != 0, // isLoginUser
            cursor.getInt(offset + 13), // type
            cursor.getInt(offset + 14), // user_type
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // mobile
            cursor.getInt(offset + 16) // school_id
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
        entity.setAge(cursor.getInt(offset + 6));
        entity.setEndtime(cursor.getInt(offset + 7));
        entity.setUpload_token(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPicture_token(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGrade(cursor.getInt(offset + 10));
        entity.setAuth_token(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setIsLoginUser(cursor.getShort(offset + 12) != 0);
        entity.setType(cursor.getInt(offset + 13));
        entity.setUser_type(cursor.getInt(offset + 14));
        entity.setMobile(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setSchool_id(cursor.getInt(offset + 16));
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
