package com.smartreader.service.db.entity;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.mark.model.bean.SRMarkBean;
import com.smartreader.ui.login.model.bean.SRUser;

import com.smartreader.service.db.entity.SRBookDao;
import com.smartreader.service.db.entity.SRMarkBeanDao;
import com.smartreader.service.db.entity.SRUserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig sRBookDaoConfig;
    private final DaoConfig sRMarkBeanDaoConfig;
    private final DaoConfig sRUserDaoConfig;

    private final SRBookDao sRBookDao;
    private final SRMarkBeanDao sRMarkBeanDao;
    private final SRUserDao sRUserDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        sRBookDaoConfig = daoConfigMap.get(SRBookDao.class).clone();
        sRBookDaoConfig.initIdentityScope(type);

        sRMarkBeanDaoConfig = daoConfigMap.get(SRMarkBeanDao.class).clone();
        sRMarkBeanDaoConfig.initIdentityScope(type);

        sRUserDaoConfig = daoConfigMap.get(SRUserDao.class).clone();
        sRUserDaoConfig.initIdentityScope(type);

        sRBookDao = new SRBookDao(sRBookDaoConfig, this);
        sRMarkBeanDao = new SRMarkBeanDao(sRMarkBeanDaoConfig, this);
        sRUserDao = new SRUserDao(sRUserDaoConfig, this);

        registerDao(SRBook.class, sRBookDao);
        registerDao(SRMarkBean.class, sRMarkBeanDao);
        registerDao(SRUser.class, sRUserDao);
    }
    
    public void clear() {
        sRBookDaoConfig.clearIdentityScope();
        sRMarkBeanDaoConfig.clearIdentityScope();
        sRUserDaoConfig.clearIdentityScope();
    }

    public SRBookDao getSRBookDao() {
        return sRBookDao;
    }

    public SRMarkBeanDao getSRMarkBeanDao() {
        return sRMarkBeanDao;
    }

    public SRUserDao getSRUserDao() {
        return sRUserDao;
    }

}
