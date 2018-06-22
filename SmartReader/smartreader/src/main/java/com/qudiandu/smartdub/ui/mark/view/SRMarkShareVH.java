package com.qudiandu.smartdub.ui.mark.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.SRApplication;
import com.qudiandu.smartdub.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartdub.thirdParty.image.ZYIImageLoader;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.SRAppConstants;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.dubbing.model.bean.SRCatalogueResponse;
import com.qudiandu.smartdub.ui.myAudio.activity.SRCataloguesActivity;
import com.qudiandu.smartdub.utils.ZYToast;
import com.third.loginshare.ShareProxy;
import com.third.loginshare.ThirdPartyBase;
import com.third.loginshare.entity.ShareEntity;
import com.third.loginshare.interf.IShareCallBack;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/6/11.
 */

public class SRMarkShareVH extends ZYBaseViewHolder<SRCatalogueResponse> {

    SRCatalogueResponse mData;

    @Bind(R.id.rootView)
    LinearLayout rootView;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Override
    public void updateView(SRCatalogueResponse data, int position) {
        if (data != null) {
            mData = data;
            showView();
        } else {
            hideView();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_mark_share;
    }

    public void setTitle(String title) {
        if (textTitle != null) {
            textTitle.setText(title);
        }
    }


    public void hideView() {
        rootView.setVisibility(View.GONE);
    }

    public void showView() {
        rootView.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.rootView, R.id.textSee, R.id.imgQQ, R.id.imgWechat, R.id.imgFriend, R.id.imgSina})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rootView:
                hideView();
                break;
            case R.id.textSee:
                mContext.startActivity(SRCataloguesActivity.createIntent(mContext));
                hideView();
                break;
            case R.id.imgQQ:
                share(ShareProxy.SHARE_TYPE_QQ);
                break;
            case R.id.imgWechat:
                share(ShareProxy.SHARE_TYPE_WECHAT);
                break;
            case R.id.imgFriend:
                share(ShareProxy.SHARE_TYPE_WECHAT_FRIEND);
                break;
            case R.id.imgSina:
                share(ShareProxy.SHARE_TYPE_WEIBO);
                break;
        }
    }

    public void share(final int type) {
        hideView();

        ZYImageLoadHelper.getImageLoader().loadFromMediaStore(this, SRUserManager.getInstance().getUser().avatar, new ZYIImageLoader.OnLoadLocalImageFinishListener() {
            @Override
            public void onLoadFinish(@Nullable final Bitmap bitmap) {
                SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShareEntity shareEntity = new ShareEntity();
                        shareEntity.avatarUrl = SRUserManager.getInstance().getUser().avatar;
                        if (bitmap == null) {
                            shareEntity.avatarBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.def_avatar);
                        } else {
                            shareEntity.avatarBitmap = bitmap;
                        }
                        shareEntity.webUrl = mData.share_url;
                        shareEntity.title = SRUserManager.getInstance().getUser().nickname + " 同学的课文配音作品快来听一下吧！";
                        shareEntity.text = "专为小学设计的英语听说训练智能学习工具";

                        switch (type) {
                            case ShareProxy.SHARE_TYPE_QQ:
                                ThirdPartyBase.getInstance().initQQ(SRApplication.getInstance(), SRAppConstants.TENCENT_APP_ID);
                                break;
                            case ShareProxy.SHARE_TYPE_QZONE:
                                ThirdPartyBase.getInstance().initQQ(SRApplication.getInstance(), SRAppConstants.TENCENT_APP_ID);
                                break;
                            case ShareProxy.SHARE_TYPE_WECHAT:
                                ThirdPartyBase.getInstance().initWechat(SRApplication.getInstance(),
                                        SRAppConstants.WECHAT_APP_KEY, SRAppConstants.WECHAT_APP_SECRET);
                                break;
                            case ShareProxy.SHARE_TYPE_WECHAT_FRIEND:
                                ThirdPartyBase.getInstance().initWechat(SRApplication.getInstance(),
                                        SRAppConstants.WECHAT_APP_KEY, SRAppConstants.WECHAT_APP_SECRET);
                                break;
                            case ShareProxy.SHARE_TYPE_WEIBO:
                                ThirdPartyBase.getInstance().initWeibo(SRApplication.getInstance(),
                                        SRAppConstants.SINA_APP_KEY, SRAppConstants.SINA_SCOPE, SRAppConstants.SINA_REDIRECT_URL);
                                break;
                        }
                        ShareProxy.getInstance().share(type, SRApplication.getInstance().getCurrentActivity(), shareEntity, new IShareCallBack() {
                            @Override
                            public void onShareFailed(int errorCode, String errorMsg) {
                                ZYToast.show(mContext, "分享失败!");
                            }

                            @Override
                            public void onShareSuccess() {
                                ZYToast.show(mContext, "分享成功!");
                            }

                            @Override
                            public void onShareCancel() {
                                ZYToast.show(mContext, "分享取消!");
                            }
                        });
                    }
                });
            }
        });
    }
}
