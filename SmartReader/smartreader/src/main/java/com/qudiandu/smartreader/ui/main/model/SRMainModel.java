package com.qudiandu.smartreader.ui.main.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.main.model.bean.SRVersion;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkResponse;
import com.qudiandu.smartreader.ui.set.model.bean.SRRemind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import rx.Observable;

/**
 * Created by ZY on 17/3/16.
 */

public class SRMainModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRAdert>>> getAdverts(String type) {
        return mApi.getAdverts(type);
    }

    public Observable<ZYResponse<List<SRBook>>> getBooks(String grade_id) {
        return mApi.getBooks(grade_id);
    }

    public Observable<ZYResponse<SRVersion>> getVersion() {
        return mApi.getVersion();
    }

    public Observable<ZYResponse<SRMarkResponse>> bookAddReport(String bookIds) {
        return mApi.bookAddReport(bookIds);
    }

    public Observable<ZYResponse<List<SRClass>>> getTeacherClasss() {
        return mApi.getTeacherClasss(0, 100);
    }

    public Observable<ZYResponse<List<SRClass>>> getStudentClasss() {
        return mApi.getStudentClasss(0, 100);
    }

    public Observable<ZYResponse<List<SRTask>>> getTeacherTasks(int group_id, int start, int rows) {
        return mApi.getTeacherTasks(group_id, start, rows);
    }

    public Observable<ZYResponse<List<SRTask>>> getStudentTasks(int group_id, int start, int rows) {
        return mApi.getStudentTasks(group_id, start, rows);
    }

    public Observable<ZYResponse> addClass(String school_name, String grade, String class_name, String telephone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("school_name", school_name);
        params.put("grade", grade);
        params.put("class_name", class_name);
        params.put("telephone", telephone);
        return mApi.addClass(params);
    }

    public Observable<ZYResponse> joinClass(String group_id, String nickname, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group_id", group_id);
        params.put("nickname", nickname);
        params.put("code", code);
        return mApi.joinClass(params);
    }

    public Observable<ZYResponse<SRClass>> getClassDetail(String group_id) {
        return mApi.getClassDetail(group_id);
    }

    public Observable<ZYResponse<List<SRUser>>> getClassUsers(String group_id, int start, int rows) {
        return mApi.getClassUsers(group_id, start, rows);
    }

    public Observable<ZYResponse> removeUsers(String group_id, String del_uid) {
        return mApi.removeUsers(group_id, del_uid);
    }

    public Observable<ZYResponse> updateClassName(String group_id, String class_name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group_id", group_id);
        params.put("class_name", class_name);
        return mApi.updateClassName(params);
    }

    public Observable<ZYResponse<SRRemind>> msgRemind() {
        return mApi.msgRemind();
    }

    public Observable<ZYResponse> delTask(String task_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("task_id", task_id);
        return mApi.delTask(params);
    }
}
