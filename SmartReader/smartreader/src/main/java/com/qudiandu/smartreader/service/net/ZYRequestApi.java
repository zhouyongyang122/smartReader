package com.qudiandu.smartreader.service.net;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.html5.ZYHtml5UrlBean;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.model.bean.SRAdert;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRGrade;
import com.qudiandu.smartreader.ui.main.model.bean.SRVersion;
import com.qudiandu.smartreader.ui.mark.model.bean.SRCatalogueResponse;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkResponse;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueDetail;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.set.model.bean.SRSysMsg;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
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
}
