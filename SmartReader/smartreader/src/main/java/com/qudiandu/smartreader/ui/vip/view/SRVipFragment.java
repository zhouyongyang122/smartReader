package com.qudiandu.smartreader.ui.vip.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 18/3/1.
 */

public class SRVipFragment extends ZYBaseFragment<SRVipContract.IPresenter> implements SRVipContract.IView {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textGrade)
    TextView textGrade;

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

    SRVip.Price selectedPrice;

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
        return view;
    }

    private void initPriceView() {
        mPriceAdapter = new ZYBaseRecyclerAdapter<SRVip.Price>(mPresenter.getVip().package_list) {
            @Override
            public ZYBaseViewHolder<SRVip.Price> createViewHolder(int type) {
                return new SRVipPriceVH();
            }
        };
        mPriceAdapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SRVip.Price price = mPriceAdapter.getItem(position);
                if (price != null) {
                    selectedPrice = price;
                }
                for (int i = 0; i < mPriceAdapter.getItemCount(); i++) {
                    price = mPriceAdapter.getItem(position);
                    if (price != null) {
                        if (i == position) {
                            price.isSelected = true;
                        } else {
                            price.isSelected = false;
                        }
                    }
                }
                mRightsAdapter.notifyDataSetChanged();
            }
        });
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

    @OnClick({R.id.layoutWechat, R.id.layoutAliPay, R.id.textProtocol, R.id.btBuy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutWechat:
                btnWeichat.setSelected(true);
                btnAliPay.setSelected(false);
                break;
            case R.id.layoutAliPay:
                btnWeichat.setSelected(false);
                btnAliPay.setSelected(true);
                break;
            case R.id.textProtocol:
                break;
            case R.id.btBuy:
                showProgress();
                mPresenter.buy(selectedPrice, btnAliPay.isSelected() ? SRVipPresenter.PAY_ALIPAY_TYPE : SRVipPresenter.PAY_WECHAT_TYPE);
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
    }

    @Override
    public void buyFail() {
        hideProgress();
    }
}
