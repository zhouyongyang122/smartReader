package com.qudiandu.smartreader.service.db.entity;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskListenHistory;

import com.qudiandu.smartreader.service.db.entity.SRUserDao;
import com.qudiandu.smartreader.service.db.entity.SRBookDao;
import com.qudiandu.smartreader.service.db.entity.SRMarkBeanDao;
import com.qudiandu.smartreader.service.db.entity.SRTaskListenHistoryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig sRUserDaoConfig;
    private final DaoConfig sRBookDaoConfig;
    private final DaoConfig sRMarkBeanDaoConfig;
    private final DaoConfig sRTaskListenHistoryDaoConfig;

    private final SRUserDao sRUserDao;
    private final SRBookDao sRBookDao;
    private final SRMarkBeanDao sRMarkBeanDao;
    private final SRTaskListenHistoryDao sRTaskListenHistoryDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        sRUserDaoConfig = daoConfigMap.get(SRUserDao.class).clone();
        sRUserDaoConfig.initIdentityScope(type);

        sRBookDaoConfig = daoConfigMap.get(SRBookDao.class).clone();
        sRBookDaoConfig.initIdentityScope(type);

        sRMarkBeanDaoConfig = daoConfigMap.get(SRMarkBeanDao.class).clone();
        sRMarkBeanDaoConfig.initIdentityScope(type);

        sRTaskListenHistoryDaoConfig = daoConfigMap.get(SRTaskListenHistoryDao.class).clone();
        sRTaskListenHistoryDaoConfig.initIdentityScope(type);

        sRUserDao = new SRUserDao(sRUserDaoConfig, this);
        sRBookDao = new SRBookDao(sRBookDaoConfig, this);
        sRMarkBeanDao = new SRMarkBeanDao(sRMarkBeanDaoConfig, this);
        sRTaskListenHistoryDao = new SRTaskListenHistoryDao(sRTaskListenHistoryDaoConfig, this);

        registerDao(SRUser.class, sRUserDao);
        registerDao(SRBook.class, sRBookDao);
        registerDao(SRMarkBean.class, sRMarkBeanDao);
        registerDao(SRTaskListenHistory.class, sRTaskListenHistoryDao);
    }
    
    public void clear() {
        sRUserDaoConfig.clearIdentityScope();
        sRBookDaoConfig.clearIdentityScope();
        sRMarkBeanDaoConfig.clearIdentityScope();
        sRTaskListenHistoryDaoConfig.clearIdentityScope();
    }

    public SRUserDao getSRUserDao() {
        return sRUserDao;
    }

    public SRBookDao getSRBookDao() {
        return sRBookDao;
    }

    public SRMarkBeanDao getSRMarkBeanDao() {
        return sRMarkBeanDao;
    }

    public SRTaskListenHistoryDao getSRTaskListenHistoryDao() {
        return sRTaskListenHistoryDao;
    }

}
