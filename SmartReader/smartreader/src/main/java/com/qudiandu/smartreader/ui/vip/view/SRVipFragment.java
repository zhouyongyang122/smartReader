package com.qudiandu.smartreader.ui.vip.view;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.vip.contract.SRVipContract;
import com.qudiandu.smartreader.ui.vip.model.bean.SRVip;
import com.qudiandu.smartreader.ui.vip.presenter.SRVipPresenter;
import com.qudiandu.smartreader.ui.vip.view.viewHolder.SRVipPriceVH;
import com.qudiandu.smartreader.ui.vip.view.viewHolder.SRVipRightsVH;
import com.qudiandu.smartreader.ui.web.SRWebViewActivity;
import com.qudiandu.smartreader.utils.ZYDateUtils;
import com.qudiandu.smartreader.utils.ZYUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.qudiandu.smartreader.utils.ZYDateUtils.YYMMDDHH;

/**
 * Created by ZY on 18/3/1.
 */

public class SRVipFragment extends ZYBaseFragment<SRVipContract.IPresenter> implements SRVipContract.IView, SRVipPriceVH.VipPriceVHListener {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textVipTime)
    TextView textVipTime;

    @Bind(R.id.textProtocol)
    TextView textProtocol;

    @Bind(R.id.imgVip)
    ImageView imgVip;

    @Bind(R.id.textGrade)
    TextView textGrade;

    @Bind(R.id.btBuy)
    TextView btBuy;

    @Bind(R.id.priceRecyclerView)
    RecyclerView priceRecyclerView;
    ZYBaseRecyclerAdapter<SRVip.Price> mPriceAdapter;

    @Bind(R.id.btnWeichat)
    Button btnWeichat;

    @Bind(R.id.btnAliPay)
    Button btnAliPay;

    @Bind(R.id.rightsRecyclerView)
    RecyclerView rightsRecyclerView;
    ZYBaseRecyclerAdapter<SRVip.Rights> mRightsAdapter;

    ZYLoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.sr_fragment_vip, container, false);
        ButterKnife.bind(this, view);

        loadingView = new ZYLoadingView(mActivity);
        loadingView.attach(view);
        loadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loadingView.showLoading();

        SRUser user = SRUserManager.getInstance().getUser();
        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, user.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
        textName.setText(user.nickname);
        textGrade.setText(user.grade + "年级");

        initPriceView();
        initRightsView();

        btnWeichat.setSelected(true);

        mPresenter.loadVip();

        textProtocol.setText("确认购买即表示同意 <<趣点读会员协议>>");
        textProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textProtocol.getPaint().setAntiAlias(true);//抗锯齿
        return view;
    }

    private void initPriceView() {
        mPriceAdapter = new ZYBaseRecyclerAdapter<SRVip.Price>(mPresenter.getPriceList()) {
            @Override
            public ZYBaseViewHolder<SRVip.Price> createViewHolder(int type) {
                SRVipPriceVH priceVH = new SRVipPriceVH(SRVipFragment.this);
                return priceVH;
            }
        };
        priceRecyclerView.setAdapter(mPriceAdapter);
        priceRecyclerView.setNestedScrollingEnabled(false);
        priceRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    private void initRightsView() {
        mRightsAdapter = new ZYBaseRecyclerAdapter<SRVip.Rights>(mPresenter.getRightsList()) {
            @Override
            public ZYBaseViewHolder<SRVip.Rights> createViewHolder(int type) {
                return new SRVipRightsVH();
            }
        };
        mRightsAdapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        rightsRecyclerView.setAdapter(mRightsAdapter);
        rightsRecyclerView.setNestedScrollingEnabled(false);
        rightsRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
    }

    @OnClick({R.id.layoutWechat, R.id.layoutAliPay, R.id.textProtocol, R.id.btBuy, R.id.btnWeichat, R.id.btnAliPay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutWechat:
            case R.id.btnWeichat:
                btnWeichat.setSelected(true);
                btnAliPay.setSelected(false);
                break;
            case R.id.layoutAliPay:
            case R.id.btnAliPay:
                btnWeichat.setSelected(false);
                btnAliPay.setSelected(true);
                break;
            case R.id.textProtocol:
                if (!TextUtils.isEmpty(mPresenter.getVip().protocol_url)) {
                    startActivity(SRWebViewActivity.createIntent(getActivity(), mPresenter.getVip().protocol_url, "趣点读会员协议"));
                }
                break;
            case R.id.btBuy:
                showProgress();
                mPresenter.buy(btnAliPay.isSelected() ? SRVipPresenter.PAY_ALIPAY_TYPE : SRVipPresenter.PAY_WECHAT_TYPE);
                break;
        }
    }

    @Override
    public void showLoading() {
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        loadingView.showNothing();
    }

    @Override
    public void showList(boolean isHasMore) {
        loadingView.showNothing();
        SRUser user = SRUserManager.getInstance().getUser();
        if (user.isVip()) {
            textVipTime.setVisibility(View.VISIBLE);
            imgVip.setVisibility(View.VISIBLE);
            textVipTime.setText(ZYDateUtils.getTimeString(Long.parseLong(SRUserManager.getInstance().getUser().getVip_endtime()) * 1000, YYMMDDHH) + "到期");
        } else {
            textVipTime.setVisibility(View.GONE);
            imgVip.setVisibility(View.GONE);
        }
        btBuy.setText("确认购买(" + mPresenter.getPriceList().get(0).amount + "元)");
        mPriceAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmpty() {
        loadingView.showEmpty();
    }

    @Override
    public void buySuccess() {
        hideProgress();
        showToast("会员购买成功!");

        SRUser user = SRUserManager.getInstance().getUser();
        if (user.isVip()) {
            user.vip_endtime = (Long.parseLong(user.vip_endtime) + mPresenter.getSelectPrice().days * 24 * 60 * 60) + "";
        } else {
            user.is_vip = "1";
            user.vip_endtime = "" + mPresenter.getSelectPrice().days * 24 * 60 * 60;
        }
        SRUserManager.getInstance().setUser(user);

        finish();
    }

    @Override
    public void buyFail() {
        hideProgress();
    }

    @Override
    public void onPriceClick(SRVip.Price price) {
        if (price != null) {
            mPresenter.setSelectPrice(price);
            btBuy.setText("确认购买(" + price.amount + "元)");
        }
        for (int i = 0; i < mPriceAdapter.getItemCount(); i++) {
            SRVip.Price price_ = mPriceAdapter.getItem(i);
            if (price_ != null) {
                if (price_.id == price.id) {
                    price_.isSelected = true;
                } else {
                    price_.isSelected = false;
                }
            }
        }
        mPriceAdapter.notifyDataSetChanged();
    }
}
