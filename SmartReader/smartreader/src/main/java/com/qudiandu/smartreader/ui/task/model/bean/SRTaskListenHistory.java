package com.qudiandu.smartreader.ui.task.model.bean;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.service.db.ZYDBManager;
import com.qudiandu.smartreader.service.db.entity.SRTaskListenHistoryDao;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ZY on 17/9/21.
 */
@Entity
public class SRTaskListenHistory implements ZYIBaseBean {

    //userId + listenId
    @Id
    public String listenId;

    public int listenTime;

    static Object object = new Object();

    @Generated(hash = 50018563)
    public SRTaskListenHistory(String listenId, int listenTime) {
        this.listenId = listenId;
        this.listenTime = listenTime;
    }

    @Generated(hash = 1905287984)
    public SRTaskListenHistory() {
    }

    public long update() {
        synchronized (object) {
            SRTaskListenHistoryDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getSRTaskListenHistoryDao();
            return entityDao.insertOrReplace(this);
        }
    }

    public static SRTaskListenHistory queryById(String listenId) {
        synchronized (object) {
            SRTaskListenHistoryDao entityDao = ZYDBManager.getInstance().getReadableDaoSession().getSRTaskListenHistoryDao();
            SRTaskListenHistory listenHistory = entityDao.load(SRUserManager.getInstance().getUser().uid + "_" + listenId);
            if (listenHistory == null) {
                listenHistory = new SRTaskListenHistory();
                listenHistory.listenId = SRUserManager.getInstance().getUser().uid + "_" + listenId;
            }
            return listenHistory;
        }
    }

    public String getListenId() {
        return this.listenId;
    }

    public void setListenId(String listenId) {
        this.listenId = listenId;
    }

    public int getListenTime() {
        return this.listenTime;
    }

    public void setListenTime(int listenTime) {
        this.listenTime = listenTime;
    }
}
