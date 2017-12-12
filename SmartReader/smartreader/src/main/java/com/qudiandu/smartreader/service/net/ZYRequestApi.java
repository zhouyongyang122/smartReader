package com.qudiandu.smartreader.service.net;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.html5.ZYHtml5UrlBean;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.model.bean.SRGrade;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.main.model.bean.SRVersion;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.dubbing.model.bean.SRMarkResponse;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueDetail;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.set.model.bean.SRRemind;
import com.qudiandu.smartreader.ui.set.model.bean.SRSysMsg;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskCate;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskFinish;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyUnit;
import com.qudiandu.smartreader.ui.wordStudy.model.bean.SRWordStudyWord;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZY on 17/3/16.
 */

public interface ZYRequestApi {

    @GET("basic/advert")
    Observable<ZYResponse<List<SRAdert>>> getAdverts(@Query("type") String type);

    @GET("basic/message")
    Observable<ZYResponse<List<SRSysMsg>>> getSysMsgs(@Query("start") int start, @Query("rows") int rows);

    @GET("book/gradeList")
    Observable<ZYResponse<List<SRGrade>>> getGrades();

    @GET("basic/getVersion")
    Observable<ZYResponse<SRVersion>> getVersion();

    @GET("basic/html5")
    Observable<ZYResponse<ZYHtml5UrlBean>> getHtml5Urls();

    @GET("book/bookList")
    Observable<ZYResponse<List<SRBook>>> getBooks(@Query("grade_id") String grade_id);

    @POST("show/trackAdd")
    Observable<ZYResponse<SRMarkResponse>> trackAdd(@Body Map<String, String> params);

    //bookIds 书本id,多个逗号隔开
    @POST("book/download")
    Observable<ZYResponse<SRMarkResponse>> bookAddReport(@Query("book") String bookIds);

    @POST("user/login")
    Observable<ZYResponse<SRUser>> login(@Body Map<String, String> params);

    @POST("user/thirdLogin")
    Observable<ZYResponse<SRUser>> thirdLogin(@Body Map<String, String> params);

    @POST("user/register")
    Observable<ZYResponse<SRUser>> register(@Body Map<String, String> params);

    @POST("user/tieupMobile")
    Observable<ZYResponse<SRUser>> bindMobile(@Body Map<String, String> params);

    @POST("user/logout")
    Observable<ZYResponse<SRUser>> loginOut();

    @POST("user/editMember")
    Observable<ZYResponse<SRUser>> editUser(@Body Map<String, String> params);

    @POST("user/changePassword")
    Observable<ZYResponse<SRUser>> changePassword(@Body Map<String, String> params);

    @POST("user/resetPassword")
    Observable<ZYResponse<SRUser>> resetPassword(@Body Map<String, String> params);

    @POST("user/mobileCode")
    Observable<ZYResponse> mobileCode(@Body Map<String, String> params);

    @POST("show/catalogueDel")
    Observable<ZYResponse> delCatalogue(@Body Map<String, String> params);

    @POST("user/refreshToken")
    Observable<ZYResponse<SRUser>> refreshToken(@Body Map<String, String> params);

    @POST("basic/feedback")
    Observable<ZYResponse> feedBack(@Body Map<String, String> params);

    @POST("show/catalogueAdd")
    Observable<ZYResponse<SRCatalogueResponse>> catalogueAdd(@Body Map<String, String> params);

    @GET("show/catalogue")
    Observable<ZYResponse<List<SRCatalogueNew>>> getCatalogues(@Query("start") int start, @Query("rows") int rows);

    @GET("show/catalogueDetail")
    Observable<ZYResponse<SRCatalogueDetail>> getCatalogueDetail(@Query("show_id") String show_id);

    @GET("group/teacherGroup")
    Observable<ZYResponse<List<SRClass>>> getTeacherClasss(@Query("start") int start, @Query("rows") int rows);

    @GET("group/userGroup")
    Observable<ZYResponse<List<SRClass>>> getStudentClasss(@Query("start") int start, @Query("rows") int rows);

    @GET("group/teachTaskList")
    Observable<ZYResponse<List<SRTask>>> getTeacherTasks(@Query("group_id") int group_id, @Query("start") int start, @Query("rows") int rows);

    @GET("group/userTaskList")
    Observable<ZYResponse<List<SRTask>>> getStudentTasks(@Query("group_id") int group_id, @Query("start") int start, @Query("rows") int rows);

    @POST("group/add")
    Observable<ZYResponse> addClass(@Body Map<String, String> params);

    @POST("group/memberAdd")
    Observable<ZYResponse> joinClass(@Body Map<String, String> params);

    @GET("book/catalogue")
    Observable<ZYResponse<List<SRTaskCate>>> getTaskCates(@Query("book_id") String book_id, @Query("start") int start, @Query("rows") int rows);

    @POST("group/taskAdd")
    Observable<ZYResponse> addTask(@Body Map<String, String> params);

    @GET("group/detail")
    Observable<ZYResponse<SRClass>> getClassDetail(@Query("group_id") String group_id);

    @GET("group/memberList")
    Observable<ZYResponse<List<SRUser>>> getClassUsers(@Query("group_id") String group_id, @Query("start") int start, @Query("rows") int rows);

    @POST("group/memberDel")
    Observable<ZYResponse> removeUsers(@Query("group_id") String group_id, @Query("del_uid") String del_uid);

    @GET("group/taskFinishList")
    Observable<ZYResponse<List<SRTaskFinish>>> getTaskFinishs(@Query("group_id") String group_id, @Query("task_id") String task_id, @Query("start") int start, @Query("rows") int rows);

    @GET("group/problemFinishList")
    Observable<ZYResponse<List<SRTaskFinish>>> getProblemFinishs(@Query("group_id") String group_id, @Query("task_id") String task_id, @Query("start") int start, @Query("rows") int rows);

    @POST("group/showComment")
    Observable<ZYResponse> addComment(@Body Map<String, String> params);

    @POST("group/taskRemind")
    Observable<ZYResponse> taskRemind(@Body Map<String, String> params);

    @GET("basic/messageRemind")
    Observable<ZYResponse<SRRemind>> msgRemind();

    @POST("group/delTask")
    Observable<ZYResponse> delTask(@Body Map<String, String> params);

    @GET("group/taskProblemDetail")
    Observable<ZYResponse<SRTaskProblem>> getProblemTaskDetail(@Query("task_id") String task_id);

    @GET("group/problemFinishDetail")
    Observable<ZYResponse<SRTaskProblem>> getProblemFinishTaskDetail(@Query("finish_id") String finish_id);

    @FormUrlEncoded
    @POST("group/taskProblemFinish")
    Observable<ZYResponse> submitAnswer(@FieldMap Map<String, String> params);

    @POST("user/pushinfo")
    Observable<ZYResponse> pushInfo(@Query("registration_id") String registration_id);

    @POST("book/unit")
    Observable<ZYResponse<List<SRWordStudyUnit>>> getBookUnits(@Query("book_id") String book_id);

    @POST("book/unitWords")
    Observable<ZYResponse<List<SRWordStudyWord>>> getUnitWords(@Query("unit_id") String unit_id);
}
