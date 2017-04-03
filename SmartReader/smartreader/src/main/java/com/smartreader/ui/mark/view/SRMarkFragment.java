package com.smartreader.ui.mark.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.smartreader.base.mvp.ZYBaseRecyclerFragment;
import com.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.smartreader.thirdParty.translate.TranslateRequest;
import com.smartreader.thirdParty.translate.YouDaoBean;
import com.smartreader.ui.main.model.bean.SRTract;
import com.smartreader.ui.mark.contract.SRMarkContract;
import com.smartreader.utils.ZYLog;
import com.smartreader.utils.ZYToast;

import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkFragment extends ZYBaseRecyclerFragment<SRMarkContract.IPresenter> implements SRMarkContract.IView, SRMarkItemVH.MarkItemListener {

    ZYBaseRecyclerAdapter<SRTract> adapter;

    SRTranslateVH translateVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        adapter = new ZYBaseRecyclerAdapter<SRTract>(mPresenter.getTracks()) {
            @Override
            public ZYBaseViewHolder<SRTract> createViewHolder(int type) {
                return new SRMarkItemVH(SRMarkFragment.this);
            }
        };

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setLayoutParams(params);

        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRefreshRecyclerView.setRefreshEnable(false);
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setAdapter(adapter);

        mRefreshRecyclerView.showList(false);

        return view;
    }

    @Override
    public String getMarkId(String trackId) {
        return mPresenter.getMarkId(trackId);
    }

    @Override
    public void onShowMarkingItem(SRTract tract, int position) {
        List<SRTract> tracts = mPresenter.getTracks();
        int i = 0;
        for (SRTract value : tracts) {
            if (i != position && value.isRecordType) {
                value.isRecordType = false;
                value.isRecording = false;
                adapter.notifyItemChanged(i);
                break;
            }
            i++;
        }
    }

    @Override
    public void onTranslate(String word) {
        ZYLog.e(getClass().getSimpleName(), "translate: " + word);
        if (translateVH == null) {
            translateVH = new SRTranslateVH();
            translateVH.attachTo(mRootView);
        }
        translateVH.show(null);
        TranslateRequest.getRequest().translate(word, new TranslateRequest.TranslateRequestCallBack() {
            @Override
            public void translateCallBack(YouDaoBean translateBean, String errorMsg) {
                if (translateBean != null) {
                    translateVH.show(translateBean);
                } else {
                    ZYToast.show(mActivity, errorMsg == null ? "网络错误,请重试尝试!" : errorMsg);
                    translateVH.hide();
                }
            }
        });
    }

    public boolean onBackPressed() {
        if (translateVH != null && translateVH.isVisibility()) {
            translateVH.hide();
            return false;
        }
        return true;
    }

    @Override
    public void onMarkStart() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showWaitDialog("正在打分中....");
            }
        });
    }

    @Override
    public void onMarkEnd() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideWaitDialog();
            }
        });
    }

    @Override
    public void onMarkError(String msg) {
        ZYToast.show(mActivity, msg);
    }
}
