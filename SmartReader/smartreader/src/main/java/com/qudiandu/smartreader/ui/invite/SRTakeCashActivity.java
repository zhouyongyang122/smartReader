package com.qudiandu.smartreader.ui.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.event.SREventLogin;
import com.qudiandu.smartreader.base.mvp.ZYBaseActivity;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.thirdParty.weChat.EventWeChatAuthor;
import com.qudiandu.smartreader.thirdParty.weChat.WeChatManager;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRThridLoginParamas;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 上午10:59$
 */
public class SRTakeCashActivity extends ZYBaseActivity {

    @Bind(R.id.textMoney)
    TextView textMoney;

    @Bind(R.id.btnTake)
    TextView btnTake;

    @Bind(R.id.layoutOtherMoney)
    RelativeLayout layoutOtherMoney;

    @Bind(R.id.editMoney)
    EditText editMoney;

    float money;

    SRThridLoginParamas thridLoginParamas = new SRThridLoginParamas();
    float aviableMoney;

    public static Intent createIntent(Context context, float aviableMoney) {
        Intent intent = new Intent(context, SRTakeCashActivity.class);
        intent.putExtra("aviableMoney", aviableMoney);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sr_activity_takecash);

        aviableMoney = getIntent().getExtras().getFloat("aviableMoney");

        textMoney.setText(aviableMoney + "");

        showTitle("我要提现");

        WeChatManager.getInstance().initLogin(this);
    }

    @OnClick({R.id.textPrice1, R.id.textPrice2, R.id.textPrice3, R.id.textPrice4, R.id.textPrice5, R.id.textPrice6, R.id.textPriceOther, R.id.textCancle, R.id.textOk, R.id.btnTake})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textPrice1:
                money = 10.00f;
                break;
            case R.id.textPrice2:
                money = 30.00f;
                break;
            case R.id.textPrice3:
                money = 50.00f;
                break;
            case R.id.textPrice4:
                money = 100.00f;
                break;
            case R.id.textPrice5:
                money = 300.00f;
                break;
            case R.id.textPrice6:
                money = 500.00f;
                break;
            case R.id.textPriceOther:
                layoutOtherMoney.setVisibility(View.VISIBLE);
                break;
            case R.id.textCancle:
                layoutOtherMoney.setVisibility(View.GONE);
                break;
            case R.id.textOk:
                layoutOtherMoney.setVisibility(View.GONE);
                money = Float.parseFloat(editMoney.getText().toString());
                break;
            case R.id.btnTake:

                if (hasBindWeiChat()) {
                    takeCash();
                }
                break;
        }

        if (money > 0) {
            btnTake.setText("立即提现(" + money + "元)");
        }
    }

    private void takeCash() {
        if (money > aviableMoney) {
            showToast("提现金额不能大于可用余额哦~");
            return;
        }
        ZYNetSubscription.subscription(new SRInviteModel().withdraw("" + money), new ZYNetSubscriber() {
            @Override
            public void onSuccess(ZYResponse response) {
                showToast("提现申请成功,请耐心等待~");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        });
    }

    private boolean hasBindWeiChat() {
        if (SRUserManager.getInstance().getUser().isWechat()) {
            return true;
        }
        if (WeChatManager.getInstance().getLoginApi() == null) {
            ZYToast.show(this, "微信登录出错，请退出App,重新尝试");
        }
        //微信没有安装
        if (WeChatManager.getInstance().sendWeChatAuthRequest()) {
            showProgress();
        } else {
            ZYToast.show(this, "没有安装微信App");
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weChatAuthResponse(EventWeChatAuthor weChatAuthor) {
        if (weChatAuthor.mUserInfo != null) {
            //成功
            thridLoginParamas.setToken(weChatAuthor.mUserInfo.openid);
            thridLoginParamas.setNickname(weChatAuthor.mUserInfo.nickname);
            thridLoginParamas.setSex(weChatAuthor.mUserInfo.sex);
            thridLoginParamas.setAuth_url(WeChatManager.getInstance().getAuth_url());
            thridLoginParamas.setAvatar(weChatAuthor.mUserInfo.headimgurl);
            thridLoginParamas.setType(SRThridLoginParamas.TYPE_WECHAT);
            bindByThrid(thridLoginParamas.getParamas());
        } else {
            //失败
            showToast("微信绑定失败,请重新尝试!");
            hideProgress();
        }
    }

    public void bindByThrid(Map<String, String> paramas) {
        ZYNetSubscription.subscription(new SRInviteModel().bindThird(paramas), new ZYNetSubscriber<ZYResponse<SRUser>>() {
            @Override
            public void onSuccess(ZYResponse<SRUser> response) {
                hideProgress();
                showToast("微信绑定成功");
                SRUser user = SRUserManager.getInstance().getUser();
                user.is_wechat = "1";
                SRUserManager.getInstance().setUser(user);
                takeCash();
            }

            @Override
            public void onFail(String message) {
                hideProgress();
                super.onFail(message);
            }
        });
    }
}
