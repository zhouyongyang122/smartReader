package com.qudiandu.smartreader.ui.myAudio.model;

import com.qudiandu.smartreader.base.bean.ZYIBaseBean;
import com.qudiandu.smartreader.ui.main.model.bean.SRPage;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;

import java.util.List;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCatalogueDetail implements ZYIBaseBean {

    private int id;

    private int uid;

    private String audio;//音频地址

    private String audioPath;

    private int book_id;

    private int catalogue_id;

    private int create_time;

    private String share_url;

    private List<PageNew> page;

    public class PageNew extends SRPage {

        private int update_time;

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getCatalogue_id() {
        return catalogue_id;
    }

    public void setCatalogue_id(int catalogue_id) {
        this.catalogue_id = catalogue_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<PageNew> getPage() {
        return page;
    }

    public void setPage(List<PageNew> page) {
        this.page = page;
    }
}
