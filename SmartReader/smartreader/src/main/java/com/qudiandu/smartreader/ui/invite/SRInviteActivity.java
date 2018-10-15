package com.qudiandu.smartreader.ui.invite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.image.ZYIImageLoader;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.utils.SRShareUtils;
import com.third.loginshare.entity.ShareEntity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 上午10:56$
 */
public class SRInviteActivity extends ZYBaseActivity {

    @Bind(R.id.layoutRoot)
    RelativeLayout layoutRoot;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textTotal)
    TextView textTotal;

    @Bind(R.id.textAvailable)
    TextView textAvailable;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    ZYLoadingView mLoadingView;

    SRInCome mData;

    public static Intent createIntent(Context context) {
        return new Intent(context, SRInviteActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_invite);

        showTitle("邀请好友");

        mLoadingView = new ZYLoadingView(this);
        mLoadingView.attach(layoutRoot);
        mLoadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(true);
            }
        });

        SRUser user = SRUserManager.getInstance().getUser();
        textName.setText(user.nickname);
        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatar, R.drawable.def_avatar, R.drawable.def_avatar);

        loadData(true);
    }

    private void loadData(boolean needLoad) {
        if (needLoad) {
            mLoadingView.showLoading();
        }
        ZYNetSubscription.subscription(new SRInviteModel().getInCome(), new ZYNetSubscriber<ZYResponse<SRInCome>>() {
            @Override
            public void onSuccess(ZYResponse<SRInCome> response) {
                mLoadingView.showNothing();
                try {
                    if (response.data != null) {
                        textTotal.setText(response.data.total + "");
                        mData = response.data;
                        textAvailable.setText(response.data.available + "");
                    } else {
                        mLoadingView.showError();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFail(String message) {
                try {
                    mLoadingView.showError();
                } catch (Exception e) {

                }
            }
        });
    }

    @OnClick({R.id.btnInfo, R.id.btnWithDraw, R.id.btnInvite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInfo:
                startActivity(SRInviteInfoActivity.createIntent(this));
                break;
            case R.id.btnWithDraw:
                startActivityForResult(SRTakeCashActivity.createIntent(this, mData.available), 100);
//                if (mData != null && mData.available > 0) {
//                    startActivityForResult(SRTakeCashActivity.createIntent(this, mData.available), 100);
//                } else {
//                    showToast("没有可提现的余额哦~");
//                    return;
//                }
                break;
            case R.id.btnInvite:

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
                                shareEntity.webUrl = mData.invite_share_url;
                                shareEntity.title = SRUserManager.getInstance().getUser().nickname + " 邀请你一起学习！";
                                shareEntity.text = "专为小学设计的英语听说训练智能学习工具";
                                new SRShareUtils(SRInviteActivity.this, shareEntity).share();
                            }
                        });
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData(false);
        }
    }
}
