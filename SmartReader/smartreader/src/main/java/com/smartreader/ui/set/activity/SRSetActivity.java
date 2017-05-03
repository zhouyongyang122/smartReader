package com.smartreader.ui.set.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.html5.ZYHtml5UrlBean;
import com.smartreader.base.html5.ZYHtml5UrlRequest;
import com.smartreader.base.mvp.ZYBaseActivity;
import com.smartreader.ui.login.activity.SRRegisterActivity;
import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.ui.login.model.bean.SRUser;
import com.smartreader.ui.login.presenter.SRRegisterPresenter;
import com.smartreader.ui.login.view.SRRegisterFragment;
import com.smartreader.ui.web.SRWebViewActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/7.
 */

public class SRSetActivity extends ZYBaseActivity {

    @Bind(R.id.textChangePwd)
    TextView textChangePwd;

    @Bind(R.id.textBind)
    TextView textBind;

    @Bind(R.id.textExit)
    TextView textExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_set);

        if (SRUserManager.getInstance().isGuesterUser(false)) {
            textChangePwd.setVisibility(View.GONE);
            textBind.setVisibility(View.GONE);
            textExit.setVisibility(View.GONE);
        }

        mActionBar.showTitle("设置");

        ZYHtml5UrlRequest.getInstance().getParamas(null);
    }

    @OnClick({R.id.textProtocol, R.id.textCopyRight, R.id.textChangePwd, R.id.textBind, R.id.textExit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textProtocol:
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
            case R.id.textCopyRight:
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
            case R.id.textChangePwd:
                startActivity(SRRegisterActivity.createIntent(this, SRRegisterPresenter.CHANGE_PWD_TYPE));
                break;
            case R.id.textBind:
                startActivity(SRRegisterActivity.createIntent(this, SRRegisterPresenter.BIND_TYPE));
                break;
            case R.id.textExit:
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
            textChangePwd.setVisibility(View.VISIBLE);
            textBind.setVisibility(View.GONE);
        } else {
            textChangePwd.setVisibility(View.GONE);
            textBind.setVisibility(View.VISIBLE);
        }
    }
}
