package com.qudiandu.smartreader.ui.main.model.bean;

import android.text.TextUtils;

import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;

/**
 * Created by ZY on 17/3/28.
 */

public class SRTract implements ZYIBaseBean {

    private int track_id;

    private int catalogue_id;

    private int page_id;

    private int book_id;

    private float track_austart;

    private float track_auend;

    private double track_left;

    private double track_right;

    private double track_top;

    private double track_bottom;

    private String track_genre;

    private String track_txt;

    private int track_sort;

    private String mp3name;

    private String mp3Path;

    public boolean isRecordType;

    public boolean isRecording;

    private String markId;

    private SRMarkBean markBean;

    public String audioQiNiuKey;

    public int getTrack_id() {
        return track_id;
    }

    public void setTrack_id(int track_id) {
        this.track_id = track_id;
    }

    public int getCatalogue_id() {
        return catalogue_id;
    }

    public void setCatalogue_id(int catalogue_id) {
        this.catalogue_id = catalogue_id;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public float getTrack_austart() {
        return track_austart;
    }

    public int getAudioStart() {
        return (int) (track_austart * 1000);
    }

    public int getAudioEnd() {
        return (int) (track_auend * 1000);
    }

    public void setTrack_austart(float track_austart) {
        this.track_austart = track_austart;
    }

    public float getTrack_auend() {
        return track_auend;
    }

    public void setTrack_auend(float track_auend) {
        this.track_auend = track_auend;
    }

    public double getTrack_left() {
        return track_left;
    }

    public void setTrack_left(double track_left) {
        this.track_left = track_left;
    }

    public double getTrack_right() {
        return track_right;
    }

    public void setTrack_right(double track_right) {
        this.track_right = track_right;
    }

    public double getTrack_top() {
        return track_top;
    }

    public void setTrack_top(double track_top) {
        this.track_top = track_top;
    }

    public double getTrack_bottom() {
        return track_bottom;
    }

    public void setTrack_bottom(double track_bottom) {
        this.track_bottom = track_bottom;
    }

    public String getTrack_genre() {
        return track_genre;
    }

    public void setTrack_genre(String track_genre) {
        this.track_genre = track_genre;
    }

    public String getTrack_txt() {
        return track_txt;
    }

    public void setTrack_txt(String track_txt) {
        this.track_txt = track_txt;
    }

    public int getTrack_sort() {
        return track_sort;
    }

    public void setTrack_sort(int track_sort) {
        this.track_sort = track_sort;
    }

    public String getMp3name() {
        return mp3name;
    }

    public void setMp3name(String mp3name) {
        this.mp3name = mp3name;
    }

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    public float getTractTime() {
        String value = String.format("%.2f", track_auend - track_austart);
        return Float.valueOf(value);
    }

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public SRMarkBean getMarkBean() {
        if (markBean == null) {
            markBean = SRMarkBean.queryById(markId);
            if (markBean == null || TextUtils.isEmpty(markBean.mark_id)) {
                markBean = new SRMarkBean();
                markBean.mark_id = markId;
                markBean.audioPath = SRApplication.TRACT_AUDIO_ROOT_DIR + markId;
                markBean.audioTime = (long) (getTractTime() * 1000);
            }
        }
        return markBean;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }
}
