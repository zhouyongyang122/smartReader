package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.SRApplication;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYIImageLoader;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.activity.SRClassDetailActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.utils.SRShareUtils;
import com.qudiandu.smartreader.utils.ZYToast;
import com.third.loginshare.entity.ShareEntity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/24.
 */

public class SRClassDetailHeaderVH extends ZYBaseViewHolder<SRClass> {

    @Bind(R.id.layoutInfo)
    RelativeLayout layoutInfo;

    @Bind(R.id.textSchoolName)
    TextView textSchoolName;

    @Bind(R.id.textClassName)
    TextView textClassName;

    @Bind(R.id.textCode)
    TextView textCode;

    @Bind(R.id.textUsers)
    TextView textUsers;

    @Bind(R.id.textClassUser)
    TextView textClassUser;

    @Bind(R.id.textTip)
    TextView textTip;

    @Bind(R.id.layoutClassName)
    LinearLayout layoutClassName;

    @Bind(R.id.textClassNameUp)
    TextView textClassNameUp;

    @Bind(R.id.editClassName)
    EditText editClassName;

    SRClass mData;

    ClassDetailHeaderListener mListener;

    boolean mIsClassDetail;

    public SRClassDetailHeaderVH(ClassDetailHeaderListener listener) {
        mListener = listener;
    }

    public SRClassDetailHeaderVH(boolean isClassDetail) {
        mIsClassDetail = isClassDetail;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
    }

    @Override
    public void updateView(SRClass data, int position) {
        if (data != null) {
            mData = data;
        } else {
            mItemView.setVisibility(View.GONE);
        }

        if (textClassName != null && mData != null) {
            mItemView.setVisibility(View.VISIBLE);

            if (mIsClassDetail) {
                layoutInfo.setPadding(0, 0, 0, 0);
            }
            if (mIsClassDetail) {
                textClassUser.setVisibility(View.GONE);
                textUsers.setText("分享班级");
                textTip.setText("班级成员(" + mData.cur_num + ")");
            }
            textSchoolName.setText("学校名称: " + mData.school_name);
            textClassName.setText("班级名称: " + mData.class_name);
            textCode.setText("班级邀请码: " + mData.code);

            if (!SRUserManager.getInstance().getUser().getUid().equals(mData.uid + "")) {
                textCode.setVisibility(View.GONE);
                if (mIsClassDetail) {
                    textUsers.setVisibility(View.GONE);
                }
            }

            textClassNameUp.setText("班级名称: " + mData.class_name);
        }
    }

    public void editClassName(boolean isEdit) {
        if (isEdit) {
            layoutInfo.setVisibility(View.GONE);
            layoutClassName.setVisibility(View.VISIBLE);
            editClassName.setText("");
        } else {
            layoutInfo.setVisibility(View.VISIBLE);
            layoutClassName.setVisibility(View.GONE);
        }
    }

    public String updateClassName() {
        String upClassName = editClassName.getText().toString();
        if (TextUtils.isEmpty(upClassName) || upClassName.equals(mData.class_name)) {
            return null;
        }
        mData.class_name = upClassName;
        textClassName.setText("班级名称: " + mData.class_name);
        textClassNameUp.setText("班级名称: " + mData.class_name);
        return upClassName;
    }

    @OnClick({R.id.textCode, R.id.textUsers, R.id.textClassUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textCode:
                if (SRUserManager.getInstance().getUser().getUid().equals(mData.uid + "")) {
                    share();
                }
                break;
            case R.id.textUsers://班级详情
                if (mIsClassDetail) {
                    share();
                } else {
                    mContext.startActivity(SRClassDetailActivity.createIntent(mContext, mData));
                }
                break;
            case R.id.textClassUser:
                mListener.onClassChangeClick();
                break;
        }
    }

    private void share() {
        ZYImageLoadHelper.getImageLoader().loadFromMediaStore(this, SRUserManager.getInstance().getUser().avatar, new ZYIImageLoader.OnLoadLocalImageFinishListener() {
            @Override
            public void onLoadFinish(@Nullable final Bitmap bitmap) {
                SRApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShareEntity shareEntity = new ShareEntity();
                        shareEntity.avatarUrl = SRUserManager.getInstance().getUser().avatar;
                        if (bitmap != null) {
                            shareEntity.avatarBitmap = bitmap;
                        } else {
                            shareEntity.avatarBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                        }
                        shareEntity.webUrl = mData.share_url;
                        shareEntity.title = "快来加入 " + SRUserManager.getInstance().getUser().nickname + " 老师的班级,跟我一起作业吧!";
                        shareEntity.text = "专为小学生设计的智能学习机";
                        new SRShareUtils(SRApplication.getInstance().getCurrentActivity(), shareEntity).share();
                    }
                });
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_detail_header;
    }

    public interface ClassDetailHeaderListener {
        void onClassChangeClick();
    }
}
