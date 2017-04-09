package com.smartreader.service.net;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.ui.login.model.bean.SRUser;
import com.smartreader.ui.main.model.bean.SRAdert;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.model.bean.SRGrade;
import com.smartreader.ui.main.model.bean.SRVersion;
import com.smartreader.ui.mark.model.bean.SRMarkResponse;
import com.smartreader.ui.set.model.bean.SRSysMsg;

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
    Observable<ZYResponse<List<SRSysMsg>>> getSysMsgs();

    @GET("book/gradeList")
    Observable<ZYResponse<List<SRGrade>>> getGrades();

    @GET("basic/getVersion")
    Observable<ZYResponse<SRVersion>> getVersion();

    @GET("book/bookList")
    Observable<ZYResponse<List<SRBook>>> getBooks(@Query("grade_id") String grade_id);

    @POST("show/trackAdd")
    Observable<ZYResponse<SRMarkResponse>> trackAdd(@Body Map<String, String> params);

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

}
