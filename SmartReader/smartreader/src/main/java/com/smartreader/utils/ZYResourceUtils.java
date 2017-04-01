package com.smartreader.utils;

import com.smartreader.SRApplication;

/**
 * Created by ZY on 17/3/22.
 */

public class ZYResourceUtils {

    public static int getColor(int res) {
        return SRApplication.getInstance().getResources().getColor(res);
    }

    public static String getString(int res) {
        return SRApplication.getInstance().getString(res);
    }
}
