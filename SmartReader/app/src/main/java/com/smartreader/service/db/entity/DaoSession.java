package com.smartreader.service.db.entity;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.smartreader.service.zyNet.down.ZYDownEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig zYDownEntityDaoConfig;

    private final ZYDownEntityDao zYDownEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        zYDownEntityDaoConfig = daoConfigMap.get(ZYDownEntityDao.class).clone();
        zYDownEntityDaoConfig.initIdentityScope(type);

        zYDownEntityDao = new ZYDownEntityDao(zYDownEntityDaoConfig, this);

        registerDao(ZYDownEntity.class, zYDownEntityDao);
    }
    
    public void clear() {
        zYDownEntityDaoConfig.clearIdentityScope();
    }

    public ZYDownEntityDao getZYDownEntityDao() {
        return zYDownEntityDao;
    }

}
