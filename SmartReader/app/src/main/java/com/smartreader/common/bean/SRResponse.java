package com.smartreader.common.bean;

/**
 * Created by ZY on 17/3/16.
 */

public class SRResponse<D> implements SRIBaseBean{

    public static final int STATUS_SUCCESS  = 1;
    public static final int STATUS_FAIL     = 0;
    public static final int STATUS_403      = 403;

    public int status;

    public String msg;

    public D data;

}
