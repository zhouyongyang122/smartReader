package com.smartreader.service.zyNet.down;

import com.smartreader.service.db.ZYDBManager;
import com.smartreader.service.db.entity.ZYBaseEntity;
import com.smartreader.service.db.entity.ZYDownEntityDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by ZY on 17/3/17.
 */

@Entity
public class ZYDownEntity extends ZYBaseEntity {
    private String savePath;
    private long total;
    private long current;
    private int connectonTime = 6;
    private int stateValue;
    @Id
    private String url;
    @Transient
    private ZYDownloadService downloadService;
    /*回调监听*/
    @Transient
    private ZYDownloadScriberListener listener;

    @Generated(hash = 1400255500)
    public ZYDownEntity(String savePath, long total, long current,
                        int connectonTime, int stateValue, String url) {
        this.savePath = savePath;
        this.total = total;
        this.current = current;
        this.connectonTime = connectonTime;
        this.stateValue = stateValue;
        this.url = url;
    }

    @Generated(hash = 1754627180)
    public ZYDownEntity() {
    }

    @Override
    public void save() {
        ZYDownEntityDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownEntityDao();
        entityDao.save(this);
    }

    @Override
    public void update() {
        ZYDownEntityDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownEntityDao();
        entityDao.save(this);
    }

    @Override
    public void delete() {
        ZYDownEntityDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getZYDownEntityDao();
        entityDao.delete(this);
    }

    public static ZYDownEntity queryByUrl(String url) {
        ZYDownEntityDao entityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownEntityDao();
        QueryBuilder<ZYDownEntity> qb = entityDao.queryBuilder();
        qb.where(ZYDownEntityDao.Properties.Url.eq(url));
        List<ZYDownEntity> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static List<ZYDownEntity> queryAll() {
        ZYDownEntityDao entityDao = ZYDBManager.getInstance().getReadableDaoSession().getZYDownEntityDao();
        QueryBuilder<ZYDownEntity> qb = entityDao.queryBuilder();
        return qb.list();
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public int getConnectonTime() {
        return connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }

    public int getStateValue() {
        return stateValue;
    }

    public void setStateValue(int stateValue) {
        this.stateValue = stateValue;
    }

    public ZYDownState getState() {
        switch (stateValue) {
            case -1:
                return ZYDownState.WAIT;
            case 0:
                return ZYDownState.START;
            case 1:
                return ZYDownState.DOWNING;
            case 2:
                return ZYDownState.PAUSE;
            case 3:
                return ZYDownState.STOP;
            case 4:
                return ZYDownState.ERROR;
            case 5:
                return ZYDownState.FINISH;
            default:
                return ZYDownState.WAIT;
        }
    }

    public void setState(ZYDownState state) {
        this.stateValue = state.getState();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ZYDownloadScriberListener getListener() {
        return listener;
    }

    public void setListener(ZYDownloadScriberListener listener) {
        this.listener = listener;
    }

    public ZYDownloadService getDownloadService() {
        return downloadService;
    }

    public void setDownloadService(ZYDownloadService downloadService) {
        this.downloadService = downloadService;
    }
}
