package com.smartreader.service.net;

import com.smartreader.base.bean.ZYResponse;
import com.smartreader.ui.main.model.bean.SRAdert;
import com.smartreader.ui.main.model.bean.SRBook;
import com.smartreader.ui.main.model.bean.SRGrade;
import com.smartreader.ui.mark.model.bean.SRMarkResponse;

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

    @GET("book/gradeList")
    Observable<ZYResponse<List<SRGrade>>> getGrades();

    @GET("book/bookList")
    Observable<ZYResponse<List<SRBook>>> getBooks(@Query("grade_id") String grade_id);

    @POST("show/trackAdd")
    Observable<ZYResponse<SRMarkResponse>> trackAdd(@Body Map<String, String> params);
}
