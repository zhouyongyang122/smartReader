package com.qudiandu.smartreader.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragmentActivity;
import com.qudiandu.smartreader.thirdParty.image.ZYIImageLoader;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.presenter.SRClassDetailPresenter;
import com.qudiandu.smartreader.ui.main.view.SRClassDetailFragment;
import com.qudiandu.smartreader.utils.SRShareUtils;
import com.third.loginshare.entity.ShareEntity;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailActivity extends ZYBaseFragmentActivity<SRClassDetailFragment> {

    static final String GROUP_ID = "group_id";

    SRClassDetailPresenter presenter;

    public static Intent createIntent(Context context, String group_id) {
        Intent intent = new Intent(context, SRClassDetailActivity.class);
        intent.putExtra(GROUP_ID, group_id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitle("班级详情");
        showActionRightTitle("邀请加入", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SRClass classDetail = presenter.getClassDetail();

                ZYImageLoadHelper.getImageLoader().loadFromMediaStore(this, SRUserManager.getInstance().getUser().avatar, new ZYIImageLoader.OnLoadLocalImageFinishListener() {
                    @Override
                    public void onLoadFinish(@Nullable final Bitmap bitmap) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShareEntity shareEntity = new ShareEntity();
                                shareEntity.avatarUrl = SRUserManager.getInstance().getUser().avatar;
                                if (bitmap != null) {
                                    shareEntity.avatarBitmap = bitmap;
                                } else {
                                    shareEntity.avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                }
                                shareEntity.webUrl = classDetail.share_url;
                                shareEntity.title = "一起来加入 " + classDetail.class_name + "  班级吧!";
                                shareEntity.text = "一起来加入 " + classDetail.class_name + "  班级吧!";
                                new SRShareUtils(SRClassDetailActivity.this, shareEntity).share();
                            }
                        });
                    }
                });
            }
        });
        presenter = new SRClassDetailPresenter(mFragment, getIntent().getStringExtra(GROUP_ID));
    }

    @Override
    protected SRClassDetailFragment createFragment() {
        return new SRClassDetailFragment();
    }
}
