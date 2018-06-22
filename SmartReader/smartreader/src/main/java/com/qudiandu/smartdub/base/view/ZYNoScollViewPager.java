package com.qudiandu.smartdub.base.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ZY on 17/9/4.
 */

public class ZYNoScollViewPager extends ViewPager {

    public ZYNoScollViewPager(Context context) {
        super(context);
    }

    public ZYNoScollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
