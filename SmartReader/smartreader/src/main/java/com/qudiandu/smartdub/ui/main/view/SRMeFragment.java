package com.qudiandu.smartdub.ui.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartdub.R;
import com.qudiandu.smartdub.base.mvp.ZYBaseFragment;
import com.qudiandu.smartdub.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartdub.ui.login.model.bean.SRUser;
import com.qudiandu.smartdub.ui.login.activity.SRLoginActivity;
import com.qudiandu.smartdub.ui.login.model.SRUserManager;
import com.qudiandu.smartdub.ui.myAudio.activity.SRCataloguesActivity;
import com.qudiandu.smartdub.ui.profile.activity.SREditActivity;
import com.qudiandu.smartdub.ui.rank.acitivty.SRRankHomeActivity;
import com.qudiandu.smartdub.ui.set.activity.SRFeedBackActivity;
import com.qudiandu.smartdub.ui.set.activity.SRSetActivity;
import com.qudiandu.smartdub.ui.set.activity.SRSysMsgActivity;
import com.qudiandu.smartdub.ui.set.model.bean.SRMsgManager;
import com.qudiandu.smartdub.ui.vip.activity.SRVipActivity;
import com.qudiandu.smartdub.ui.vip.view.SRVipIconView;

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

    @Bind(R.id.imgVip)
    ImageView imgVip;

    @Bind(R.id.textVipMsg)
    TextView textVipMsg;

    @Bind(R.id.textLogin)
    TextView textLogin;

    @Bind(R.id.viewRedPoint)
    View viewRedPoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_main_me, container, false);
        ButterKnife.bind(this, view);
        refreshUserInfo();
        return view;
    }

    @OnClick({R.id.textEdit, R.id.layoutMyAudio, R.id.textLogin, R.id.layoutFeedBack, R.id.layoutSet, R.id.imgMsg, R.id.layoutMyRank, R.id.layoutMyVip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textEdit:
                mActivity.startActivity(new Intent(mActivity, SREditActivity.class));
                break;
            case R.id.textLogin:
                mActivity.startActivity(SRLoginActivity.createIntent(mActivity));
                break;
            case R.id.layoutFeedBack:
                startActivity(new Intent(mActivity, SRFeedBackActivity.class));
                break;
            case R.id.layoutSet:
                mActivity.startActivity(new Intent(mActivity, SRSetActivity.class));
                break;
            case R.id.layoutMyAudio:
                if (SRUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(SRCataloguesActivity.createIntent(mActivity));
                break;
            case R.id.imgMsg:
                mActivity.startActivity(new Intent(mActivity, SRSysMsgActivity.class));
                viewRedPoint.setVisibility(View.GONE);
                SRMsgManager.getInstance().clearMsgRemind();
                break;
            case R.id.layoutMyVip:
                if (SRUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                startActivity(SRVipActivity.createIntent(mActivity));
                break;
            case R.id.layoutMyRank:
                if (SRUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                startActivity(SRRankHomeActivity.createIntent(mActivity));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
        refreshMsgRemind();
    }

    private void refreshUserInfo() {
        if (SRUserManager.getInstance().isGuesterUser(false)) {
            textLogin.setVisibility(View.VISIBLE);
            textName.setVisibility(View.GONE);
            textGrade.setVisibility(View.GONE);
            imgVip.setVisibility(View.GONE);
            textVipMsg.setText("未开通");
            textEdit.setVisibility(View.GONE);
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, "", R.drawable.def_avatar, R.drawable.def_avatar);
        } else {
            textLogin.setVisibility(View.GONE);
            textEdit.setVisibility(View.VISIBLE);
            textName.setVisibility(View.VISIBLE);
            textGrade.setVisibility(View.VISIBLE);
            SRUser user = SRUserManager.getInstance().getUser();
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(user.getNickname());
            textGrade.setText(user.getGrade() + "年级");
            if (SRUserManager.getInstance().getUser().isVip()) {
                textVipMsg.setText("已开通");
            } else {
                textVipMsg.setText("未开通");
            }
            SRVipIconView.showVipIcon(imgVip, SRUserManager.getInstance().getUser().isVip() ? Integer.parseInt(SRUserManager.getInstance().getUser().is_vip) : 0);
        }
    }

    public void refreshMsgRemind() {
        if (SRMsgManager.getInstance().hasMsgRemind() && viewRedPoint != null) {
            viewRedPoint.setVisibility(View.VISIBLE);
        }
    }
}
