package com.qudiandu.smartdub.ui.set.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.mvp.ZYBaseActivity;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.set.model.bean.SRSysMsg;

import butterknife.Bind;

/**
 * Created by ZY on 17/4/7.
 */

public class SRMsgDetailActivity extends ZYBaseActivity {

    public static Intent getIntent(Context context, SRSysMsg msg) {
        Intent intent = new Intent(context, SRMsgDetailActivity.class);
        intent.putExtra("msg", msg);
        return intent;
    }

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.textMsg)
    TextView textMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_text);


        showActionRightTitle("回复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SRMsgDetailActivity.this, SRFeedBackActivity.class));
            }
        });

        SRSysMsg data = (SRSysMsg) getIntent().getSerializableExtra("msg");

        showTitle(data.title);
        ZYImageLoadHelper.getImageLoader().setFitType(ZYImageLoadHelper.FIT_CENTER).loadImage(this, imgBg, data.pic, R.drawable.img_default_pic, R.drawable.img_default_pic);
        textMsg.setText(data.content);
    }
}
