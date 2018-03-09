package com.qudiandu.smartreader.ui.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.contract.SRHomeContract;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRHomeBookVH;
import com.qudiandu.smartreader.ui.set.activity.SRSysMsgActivity;
import com.qudiandu.smartreader.ui.set.model.bean.SRMsgManager;
import com.qudiandu.smartreader.ui.vip.view.SRVipIconView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/16.
 */

public class SRHomeFragment extends ZYBaseFragment<SRHomeContract.IPresenter> implements SRHomeContract.IView {

    @Bind(R.id.layout_module_root)
    LinearLayout layout_module_root;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.layoutUser)
    LinearLayout layoutUser;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.imgVip)
    ImageView imgVip;

    @Bind(R.id.viewRedPoint)
    View viewRedPoint;

    SRHomeBookVH bookVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_main_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showBook(SRBook book) {
        if (bookVH == null) {
            bookVH = new SRHomeBookVH();
            bookVH.bindView(LayoutInflater.from(mActivity).inflate(bookVH.getLayoutResId(), layout_module_root, false));
            layout_module_root.addView(bookVH.getItemView());
        }

        if (TextUtils.isEmpty(book.getGrade())) {
            book.setGrade("");
        }

        bookVH.updateView(book, 0);

        textTitle.setText(book.getGrade() + "." + book.getName());
    }

    @OnClick({R.id.imgMsg})
    public void onClick(View view) {
        mActivity.startActivity(new Intent(mActivity, SRSysMsgActivity.class));
        viewRedPoint.setVisibility(View.GONE);
        SRMsgManager.getInstance().clearMsgRemind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadBook();
        refreshMsgRemind();

        if (!SRUserManager.getInstance().isGuesterUser(false)) {
            SRUser user = SRUserManager.getInstance().getUser();
            layoutUser.setVisibility(View.VISIBLE);
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
            textName.setText(user.nickname);
            SRVipIconView.showVipIcon(imgVip, user.isVip() ? Integer.parseInt(user.is_vip) : 0);
        } else {
            layoutUser.setVisibility(View.GONE);
        }
    }

    public void refreshMsgRemind() {
        if (SRMsgManager.getInstance().hasMsgRemind() && viewRedPoint != null) {
            viewRedPoint.setVisibility(View.VISIBLE);
        }
    }
}
