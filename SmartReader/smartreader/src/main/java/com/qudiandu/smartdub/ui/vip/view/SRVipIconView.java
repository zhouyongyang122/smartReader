package com.qudiandu.smartdub.ui.vip.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.utils.ZYScreenUtils;

/**
 * Created by ZY on 18/3/9.
 */

public class SRVipIconView {

    public static void showVipIcon(ImageView imageView, int is_vip) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        if (is_vip == 0) {
            imageView.setVisibility(View.GONE);
        } else if (is_vip == 1) {
            imageView.setVisibility(View.VISIBLE);
            layoutParams.width = ZYScreenUtils.dp2px(imageView.getContext(), 26);
            layoutParams.height = ZYScreenUtils.dp2px(imageView.getContext(), 20);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.icon_ordinarymember);
        } else {
            imageView.setVisibility(View.VISIBLE);
            layoutParams.width = ZYScreenUtils.dp2px(imageView.getContext(), 32);
            layoutParams.height = ZYScreenUtils.dp2px(imageView.getContext(), 16);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.icon_annualmember);
        }
    }

    public static void showVipIconRe(ImageView imageView, int is_vip) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        if (is_vip == 0) {
            imageView.setVisibility(View.GONE);
        } else if (is_vip == 1) {
            imageView.setVisibility(View.VISIBLE);
            layoutParams.width = ZYScreenUtils.dp2px(imageView.getContext(), 26);
            layoutParams.height = ZYScreenUtils.dp2px(imageView.getContext(), 20);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.icon_ordinarymember);
        } else {
            imageView.setVisibility(View.VISIBLE);
            layoutParams.width = ZYScreenUtils.dp2px(imageView.getContext(), 32);
            layoutParams.height = ZYScreenUtils.dp2px(imageView.getContext(), 16);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.icon_annualmember);
        }
    }
}
