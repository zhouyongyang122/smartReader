package com.qudiandu.smartreader.ui.task.model;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseModel;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by ZY on 17/7/23.
 */

public class SRTaskModel extends ZYBaseModel {

    public Observable<ZYResponse<List<SRTaskCate>>> getTaskCates(String book_id, int start, int rows) {
        return mApi.getTaskCates(book_id, start, rows);
    }

    public Observable<ZYResponse> addTask(String group_id, String book_id, String catalogue_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group_id", group_id);
        params.put("book_id", book_id);
        params.put("catalogue_id", catalogue_id);
        return mApi.addTask(params);
    }

    public Observable<ZYResponse<List<SRTaskFinish>>> getTaskFinishs(String group_id, String task_id, int start, int rows) {
        return mApi.getTaskFinishs(group_id, task_id, start, rows);
    }

    public Observable<ZYResponse<List<SRTaskFinish>>> getProblemFinishs(String group_id, String task_id, int start, int rows) {
        return mApi.getProblemFinishs(group_id, task_id, start, rows);
    }

    public Observable<ZYResponse> addComment(String show_id, String comment) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("show_id", show_id);
        params.put("comment", comment);
        return mApi.addComment(params);
    }

    public Observable<ZYResponse> taskRemind(String group_id, String task_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group_id", group_id);
        params.put("task_id", task_id);
        return mApi.taskRemind(params);
    }

    public Observable<ZYResponse<SRTaskProblem>> getProblemTaskDetail(String task_id) {
        return mApi.getProblemTaskDetail(task_id);
    }

    public Observable<ZYResponse<SRTaskProblem>> getProblemFinishTaskDetail(String finish_id){
        return mApi.getProblemFinishTaskDetail(finish_id);
    }

    public Observable<ZYResponse> submitAnswer(int task_id, int group_id, String answer) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("task_id", task_id + "");
        params.put("group_id", group_id + "");
        params.put("answer", answer);
        return mApi.submitAnswer(params);
    }
}
