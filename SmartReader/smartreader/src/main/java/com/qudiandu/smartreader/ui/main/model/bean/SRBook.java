package com.qudiandu.smartreader.ui.main.model.bean;

import com.qudiandu.smartreader.service.db.ZYDBManager;
import com.qudiandu.smartreader.service.db.entity.SRBookDao;
import com.qudiandu.smartreader.service.db.entity.ZYBaseEntity;
import com.qudiandu.smartreader.service.downNet.down.ZYDownState;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadScriberListener;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadService;
import com.qudiandu.smartreader.service.downNet.down.ZYIDownBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by ZY on 17/3/27.
 */

@Entity
public class SRBook extends ZYBaseEntity implements ZYIDownBase {

    @Id
    public String book_id;

    public String name;

    public String grade_id;

    public String pic;

    public String grade;

    public String zip;

    public String update_time;

    @Transient
    public List<SRCatalogue> catalogue;

    @Transient
    public List<SRPage> page;

    public String savePath;

    public long total;

    public long current;

    public int connectonTime = 6;

    public int stateValue;

    public int lastPageIndex;

    @Transient
    public ZYDownloadService downloadService;
    /*回调监听*/
    @Transient
    public ZYDownloadScriberListener listener;

    @Transient
    public boolean isCheck;

    @Transient
    public boolean isCanDelete;

    @Transient
    public boolean isDeleteStatus;


    @Generated(hash = 943368)
    public SRBook(String book_id, String name, String grade_id, String pic, String grade, String zip,
            String update_time, String savePath, long total, long current, int connectonTime,
            int stateValue, int lastPageIndex) {
        this.book_id = book_id;
        this.name = name;
        this.grade_id = grade_id;
        this.pic = pic;
        this.grade = grade;
        this.zip = zip;
        this.update_time = update_time;
        this.savePath = savePath;
        this.total = total;
        this.current = current;
        this.connectonTime = connectonTime;
        this.stateValue = stateValue;
        this.lastPageIndex = lastPageIndex;
    }

    @Generated(hash = 888537386)
    public SRBook() {
    }


    @Override
    public long save() {
        SRBookDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getSRBookDao();
        return entityDao.insertOrReplace(this);
    }

    @Override
    public long update() {
        SRBookDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getSRBookDao();
        return entityDao.insertOrReplace(this);
    }

    @Override
    public void delete() {
        SRBookDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getSRBookDao();
        entityDao.delete(this);
    }

    public static SRBook queryById(String book_id) {
        SRBookDao entityDao = ZYDBManager.getInstance().getReadableDaoSession().getSRBookDao();
        return entityDao.load(book_id);
    }

    public static SRBook queryByBookIdAndGradeId(int book_id, String grade_id) {
        SRBookDao entityDao = ZYDBManager.getInstance().getReadableDaoSession().getSRBookDao();
        QueryBuilder<SRBook> qb = entityDao.queryBuilder();
        qb.where(SRBookDao.Properties.Book_id.eq(book_id), SRBookDao.Properties.Grade_id.eq(grade_id));
        List<SRBook> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static List<SRBook> queryAll() {
        SRBookDao entityDao = ZYDBManager.getInstance().getReadableDaoSession().getSRBookDao();
        QueryBuilder<SRBook> qb = entityDao.queryBuilder();
        return qb.list();
    }

    @Override
    public String getUrl() {
        return zip;
    }

    @Override
    public void setUrl(String url) {
        zip = url;
    }

    public String getBook_id() {
        return book_id;
    }

    public int getBook_id_int() {
        try {
            return Integer.valueOf(book_id);
        } catch (Exception e) {

        }
        return 0;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }

    public String getPic() {
        return pic;
    }

    public List<SRPage> getPage() {
        return page;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<SRCatalogue> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(List<SRCatalogue> catalogue) {
        this.catalogue = catalogue;
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
            case 0:
                return ZYDownState.WAIT;
            case 1:
                return ZYDownState.START;
            case 2:
                return ZYDownState.DOWNING;
            case 3:
                return ZYDownState.PAUSE;
            case 4:
                return ZYDownState.STOP;
            case 5:
                return ZYDownState.ERROR;
            case 6:
                return ZYDownState.FINISH;
            case 7:
                return ZYDownState.UNZIP;
            case 8:
                return ZYDownState.UNZIPERROR;
            default:
                return ZYDownState.WAIT;
        }
    }

    public String getStateString() {
        switch (stateValue) {
            case 0:
                return "准备下载";
            case 1:
                return "准备下载";
            case 2:
                return "下载中";
            case 3:
                return "已暂停";
            case 4:
                return "已暂停";
            case 5:
                return "下载出错";
            case 6:
                return "下载完成";
            case 7:
                return "解压中";
            case 8:
                return "解压出错";
            default:
                return "准备下载";
        }
    }

    public boolean isFinished() {
        return stateValue == ZYDownState.FINISH.getState();
    }

    public void setState(ZYDownState state) {
        this.stateValue = state.getState();
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

    @Override
    public String getId() {
        return book_id;
    }

    @Override
    public String toString() {
        return "SRBook{" +
                "book_id=" + book_id +
                ", name='" + name + '\'' +
                ", grade_id='" + grade_id + '\'' +
                ", pic='" + pic + '\'' +
                ", grade='" + grade + '\'' +
                ", zip='" + zip + '\'' +
                ", update_time='" + update_time + '\'' +
                ", savePath='" + savePath + '\'' +
                ", total=" + total +
                ", current=" + current +
                ", connectonTime=" + connectonTime +
                ", stateValue=" + stateValue +
                '}';
    }

    public int getLastPageIndex() {
        return this.lastPageIndex;
    }

    public void setLastPageIndex(int lastPageIndex) {
        this.lastPageIndex = lastPageIndex;
    }
}