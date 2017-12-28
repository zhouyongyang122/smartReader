package com.qudiandu.smartreader.utils;

import android.widget.TextView;

import com.qudiandu.smartreader.SRApplication;

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

    public static void blodTextView(TextView textView) {
        textView.getPaint().setFakeBoldText(true);
    }
}
