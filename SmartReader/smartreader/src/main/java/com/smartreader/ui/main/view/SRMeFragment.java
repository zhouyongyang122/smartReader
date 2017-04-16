package com.smartreader.ui.main.view;

import android.content.Intent;
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
import com.smartreader.base.event.SREventEditSuc;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.smartreader.ui.login.model.bean.SRUser;
import com.smartreader.ui.login.activity.SRLoginActivity;
import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.ui.profile.activity.SREditActivity;
import com.smartreader.ui.set.activity.SRFeedBackActivity;
import com.smartreader.ui.set.activity.SRSetActivity;
import com.smartreader.ui.set.activity.SRSysMsgActivity;
import com.smartreader.ui.web.SRWebViewActivity;
import com.smartreader.utils.ZYScreenUtils;
import com.smartreader.utils.ZYToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

        refreshUserInfo();

        return view;
    }

    @OnClick({R.id.textEdit, R.id.imgMsg, R.id.textLogin, R.id.textFeedBack, R.id.textSet, R.id.textShare})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textEdit:
                mActivity.startActivity(new Intent(mActivity, SREditActivity.class));
                break;
            case R.id.imgMsg:
                mActivity.startActivity(new Intent(mActivity, SRSysMsgActivity.class));
                break;
            case R.id.textLogin:
                mActivity.startActivity(SRLoginActivity.createIntent(mActivity));
                break;
            case R.id.textFeedBack:
                startActivity(new Intent(mActivity, SRFeedBackActivity.class));
                break;
            case R.id.textSet:
                mActivity.startActivity(new Intent(mActivity, SRSetActivity.class));
                break;
            case R.id.textShare:
                ZYToast.show(mActivity, "分享？分享什么?");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        refreshUserInfo();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventEditSuc(SREventEditSuc eventEditSuc) {
        refreshUserInfo();
    }
}
