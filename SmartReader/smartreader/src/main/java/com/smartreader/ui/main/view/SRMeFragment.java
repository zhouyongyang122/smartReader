package com.smartreader.ui.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.smartreader.ui.login.model.bean.SRUser;
import com.smartreader.ui.login.activity.SRLoginActivity;
import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.utils.ZYScreenUtils;
import com.smartreader.utils.ZYToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/16.
 */

public class SRMeFragment extends ZYBaseFragment {

    @Bind(R.id.layoutBg)
    RelativeLayout layoutBg;

    @Bind(R.id.imgBg)
    ImageView imgBg;

    @Bind(R.id.imgMsg)
    ImageView imgMsg;

    @Bind(R.id.layoutUserInfo)
    RelativeLayout layoutUserInfo;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textGrade)
    TextView textGrade;

    @Bind(R.id.textEdit)
    TextView textEdit;

    @Bind(R.id.textLogin)
    TextView textLogin;

    @Bind(R.id.textFeedBack)
    TextView textFeedBack;

    @Bind(R.id.textSet)
    TextView textSet;

    @Bind(R.id.textShare)
    TextView textShare;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_main_me, container, false);
        ButterKnife.bind(this, view);

        LinearLayout.LayoutParams layoutBgParams = (LinearLayout.LayoutParams) layoutBg.getLayoutParams();
        float scale = 375.0f / 238.0f;
        layoutBgParams.height = (int) ((float) ZYScreenUtils.getScreenWidth(mActivity) / scale);

        return view;
    }

    @OnClick({R.id.imgBg, R.id.textEdit, R.id.imgMsg, R.id.textLogin, R.id.textFeedBack, R.id.textSet, R.id.textShare})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBg:
                ZYToast.show(mActivity, "想修改背景?有这功能?");
                break;
            case R.id.textEdit:
                ZYToast.show(mActivity, "想编辑? 编辑界面了?");
                break;
            case R.id.imgMsg:
                ZYToast.show(mActivity, "稍等，还没有实现");
                break;
            case R.id.textLogin:
                mActivity.startActivity(SRLoginActivity.createIntent(mActivity));
                break;
            case R.id.textFeedBack:
                ZYToast.show(mActivity, "想反馈?也没有界面");
                break;
            case R.id.textSet:
                ZYToast.show(mActivity, "设置?设置什么?");
                break;
            case R.id.textShare:
                ZYToast.show(mActivity, "分享？分享什么?");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
    }

    private void refreshUserInfo() {
        if (SRUserManager.getInstance().isGuesterUser(false)) {
            layoutUserInfo.setVisibility(View.GONE);
            textLogin.setVisibility(View.VISIBLE);
        } else {
            layoutUserInfo.setVisibility(View.VISIBLE);
            textLogin.setVisibility(View.GONE);
            SRUser user = SRUserManager.getInstance().getUser();
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(user.getNickname());
            textGrade.setText(user.getGrade() + "年级");
        }
    }
}
