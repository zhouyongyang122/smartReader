package com.qudiandu.smartreader.ui.set.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.html5.ZYHtml5UrlBean;
import com.qudiandu.smartreader.base.html5.ZYHtml5UrlRequest;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.ui.login.activity.SRRegisterActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.login.presenter.SRRegisterPresenter;
import com.qudiandu.smartreader.ui.web.SRWebViewActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/7.
 */

public class SRSetActivity extends ZYBaseActivity {

    @Bind(R.id.layoutChangePwd)
    RelativeLayout layoutChangePwd;

    @Bind(R.id.layoutBind)
    RelativeLayout layoutBind;

    @Bind(R.id.layoutExit)
    RelativeLayout layoutExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_set);

        mActionBar.showTitle("设置");

        ZYHtml5UrlRequest.getInstance().getParamas(null);
    }

    @OnClick({R.id.layoutProtocol, R.id.layoutCopyRight, R.id.layoutChangePwd, R.id.layoutBind, R.id.layoutExit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutProtocol:
                ZYHtml5UrlRequest.getInstance().getParamas(new ZYHtml5UrlRequest.Html5UrlRequestListener() {
                    @Override
                    public void onHtm5UrlRequestStart() {
                        showProgress();
                    }

                    @Override
                    public void onHtm5UrlResponsed(ZYHtml5UrlBean urlBean, String errorMsg) {
                        hideProgress();
                        if (urlBean != null) {
                            startActivity(SRWebViewActivity.createIntent(SRSetActivity.this, urlBean.use_protocol, "使用协议"));
                        } else {
                            showToast(errorMsg);
                        }
                    }
                });
                break;
            case R.id.layoutCopyRight:
                ZYHtml5UrlRequest.getInstance().getParamas(new ZYHtml5UrlRequest.Html5UrlRequestListener() {
                    @Override
                    public void onHtm5UrlRequestStart() {
                        showProgress();
                    }

                    @Override
                    public void onHtm5UrlResponsed(ZYHtml5UrlBean urlBean, String errorMsg) {
                        hideProgress();
                        if (urlBean != null) {
                            startActivity(SRWebViewActivity.createIntent(SRSetActivity.this, urlBean.copyright_url, "版权申明"));
                        } else {
                            showToast(errorMsg);
                        }
                    }
                });
                break;
            case R.id.layoutChangePwd:
                startActivity(SRRegisterActivity.createIntent(this, SRRegisterPresenter.CHANGE_PWD_TYPE));
                break;
            case R.id.layoutBind:
                startActivity(SRRegisterActivity.createIntent(this, SRRegisterPresenter.BIND_TYPE));
                break;
            case R.id.layoutExit:
                new AlertDialog.Builder(this).setTitle("退出登录").setMessage("确认退出当前账号?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SRUserManager.getInstance().loginOut();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SRUser user = SRUserManager.getInstance().getUser();
        if (user.type <= 1) {
            layoutChangePwd.setVisibility(View.VISIBLE);
            layoutBind.setVisibility(View.GONE);
        } else {
            layoutChangePwd.setVisibility(View.GONE);
            layoutBind.setVisibility(View.VISIBLE);
        }

        if (SRUserManager.getInstance().isGuesterUser(false)) {
            layoutChangePwd.setVisibility(View.GONE);
            layoutBind.setVisibility(View.GONE);
            layoutExit.setVisibility(View.GONE);
        }
    }
}
