package com.qudiandu.smartreader.ui.mark.model.bean;

import android.text.TextUtils;

import com.qudiandu.smartreader.service.db.ZYDBManager;
import com.qudiandu.smartreader.service.db.entity.SRMarkBeanDao;
import com.qudiandu.smartreader.service.db.entity.ZYBaseEntity;
import com.qudiandu.smartreader.thirdParty.xiansheng.XSBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;

/**
 * Created by ZY on 17/4/2.
 */

@Entity
public class SRMarkBean extends ZYBaseEntity {

    @Id
    public String mark_id;//mark_id由  book_id + page_id + track_Id组成

    public int score;

    public long audioTime;

    public String audioPath;

    public String share_url;//分享时 服务器返回的分享路径

    public String show_track_id;//分享时 服务器返回的id

    public String jsonValue;//打分结果

    @Transient
    private XSBean xsBean;

    @Transient
    public String value;

    @Generated(hash = 540598131)
    public SRMarkBean(String mark_id, int score, long audioTime, String audioPath, String share_url,
            String show_track_id, String jsonValue) {
        this.mark_id = mark_id;
        this.score = score;
        this.audioTime = audioTime;
        this.audioPath = audioPath;
        this.share_url = share_url;
        this.show_track_id = show_track_id;
        this.jsonValue = jsonValue;
    }

    @Generated(hash = 1822869650)
    public SRMarkBean() {
    }

    @Override
    public long save() {
        SRMarkBeanDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getSRMarkBeanDao();
        return entityDao.insertOrReplace(this);
    }

    @Override
    public long update() {
        SRMarkBeanDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getSRMarkBeanDao();
        return entityDao.insertOrReplace(this);
    }

    @Override
    public void delete() {
        SRMarkBeanDao entityDao = ZYDBManager.getInstance().getWritableDaoSession().getSRMarkBeanDao();
        entityDao.delete(this);
    }

    public static SRMarkBean queryById(String mark_id) {
        SRMarkBeanDao entityDao = ZYDBManager.getInstance().getReadableDaoSession().getSRMarkBeanDao();
        return entityDao.load(mark_id);
    }

    public String getMark_id() {
        return this.mark_id;
    }

    public void setMark_id(String mark_id) {
        this.mark_id = mark_id;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAudioPath() {
        return this.audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getShare_url() {
        return this.share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShow_track_id() {
        return this.show_track_id;
    }

    public void setShow_track_id(String show_track_id) {
        this.show_track_id = show_track_id;
    }

    public long getAudioTime() {
        return this.audioTime;
    }

    public void setAudioTime(long audioTime) {
        this.audioTime = audioTime;
    }

    public static String getMarkId(String bookId, String pageId, String tractId) {
        return bookId + "_" + pageId + "_" + tractId;
    }

    public void setScoreBean(XSBean bean) {
        xsBean = bean;
    }

    public XSBean getScoreBean() {
        if (xsBean == null) {
            if (TextUtils.isEmpty(jsonValue)) {
                return null;
            }
            xsBean = XSBean.createXSBean(jsonValue);
        }
        return xsBean;
    }

    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<String>();
        if (!TextUtils.isEmpty(value)) {
            String[] valueStrs = value.split(" ");
            for (String str : valueStrs) {
                int strInt = str.charAt(0);
                if ((strInt > 64 && strInt < 91)
                        || (strInt > 96 && strInt < 123)
                        || (strInt > 47 && strInt < 58)) {
                    values.add(str);
                }
            }
        }
        return values;
    }

    public String getJsonValue() {
        return this.jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }
}
