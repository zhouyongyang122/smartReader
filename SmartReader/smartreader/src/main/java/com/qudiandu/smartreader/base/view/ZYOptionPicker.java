package com.qudiandu.smartreader.base.view;

import android.app.Activity;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.utils.ZYResourceUtils;

import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by ZY on 17/8/30.
 * 单项选择
 */

public class ZYOptionPicker extends OptionPicker {

    public ZYOptionPicker(Activity activity, String[] items, OptionPicker.OnOptionPickListener pickListener) {
        super(activity, items);
        init(pickListener);
    }

    public ZYOptionPicker(Activity activity, List<String> items, OptionPicker.OnOptionPickListener pickListener) {
        super(activity, items);
        init(pickListener);
    }

    private void init(OptionPicker.OnOptionPickListener pickListener) {
        setOnOptionPickListener(pickListener);
        setDividerRatio(WheelView.DividerConfig.FILL);
        setTextSize(18);
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setThick(1);
        setDividerConfig(config);
        setDividerColor(ZYResourceUtils.getColor(R.color.c16));
        setTopLineColor(ZYResourceUtils.getColor(R.color.c7));
        setTextColor(ZYResourceUtils.getColor(R.color.c6));
        setCancelTextColor(ZYResourceUtils.getColor(R.color.c6));
        setSubmitTextColor(ZYResourceUtils.getColor(R.color.c6));
    }
}
