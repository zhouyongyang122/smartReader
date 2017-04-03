package com.smartreader.thirdParty.translate;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhouyongyang on 17/1/18.
 */

public interface YouDaoRequestApi {

    public static final String DOCTYPE = "json";

    public static final String KEY = "1830858689uH34fHDMkT";
//@Query("key") String key

    public static final String TYPE = "data";

    public static final String VERSION = "2.0";

    @GET("/paidapi/fanyiapi")
    Observable<YouDaoBean> getWordDetail(@Query("doctype") String doctype, @Query("key") String key, @Query("type") String type, @Query("version") String version, @Query("q") String word);
}
