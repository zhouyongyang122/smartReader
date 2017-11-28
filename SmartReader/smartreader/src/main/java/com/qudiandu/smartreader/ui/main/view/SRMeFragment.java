package com.qudiandu.smartreader.ui.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCataloguesActivity;
import com.qudiandu.smartreader.ui.profile.activity.SREditActivity;
import com.qudiandu.smartreader.ui.set.activity.SRFeedBackActivity;
import com.qudiandu.smartreader.ui.set.activity.SRSetActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/16.
 */

public class SRMeFragment extends ZYBaseFragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_main_me, container, false);
        ButterKnife.bind(this, view);
        refreshUserInfo();
        return view;
    }

    @OnClick({R.id.textEdit, R.id.textMyAudio, R.id.textLogin, R.id.textFeedBack, R.id.textSet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textEdit:
                mActivity.startActivity(new Intent(mActivity, SREditActivity.class));
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
            case R.id.textMyAudio:
                if (SRUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(SRCataloguesActivity.createIntent(mActivity));
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
            textLogin.setVisibility(View.VISIBLE);
            textName.setVisibility(View.GONE);
            textGrade.setVisibility(View.GONE);
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, "", R.drawable.def_avatar, R.drawable.def_avatar);
        } else {
            textLogin.setVisibility(View.GONE);
            SRUser user = SRUserManager.getInstance().getUser();
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(user.getNickname());
            textGrade.setText(user.getGrade() + "年级");
        }
    }
}
