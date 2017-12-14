package com.qudiandu.smartreader.ui.myAudio.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.activity.SRMainActivity;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.myAudio.contract.SRCataloguesContract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.myAudio.view.viewHolder.SRCataloguesVH;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;
import com.qudiandu.smartreader.utils.ZYToast;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesFragment extends ZYListDateFragment<SRCataloguesContract.IPresenter, SRCatalogueNew> implements SRCataloguesContract.IView {

    protected TextView mDelView;

    @Override
    protected void onItemClick(View view, int position) {
        SRCatalogueNew catalogueNew = mAdapter.getItem(position);
        if (catalogueNew != null) {
            if (catalogueNew.isEdit()) {
                mPresenter.changeSelectedValue(catalogueNew.isSeleted ? -1 : position);
                mAdapter.notifyDataSetChanged();
                return;
            }
            mActivity.startActivity(SRCatalogueDetailActivity.createIntent(mActivity, catalogueNew.getId() + ""));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDelView = new TextView(mActivity);
        mDelView.setBackgroundResource(R.color.white);
        mDelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SRCatalogueNew data = mPresenter.getDelData();
                if (data == null) {
                    ZYToast.show(mActivity, "请先选择要删除的任务!");
                    return;
                }
                onDelClick(mPresenter.getDelData());
            }
        });
        mDelView.setTextColor(ZYResourceUtils.getColor(R.color.c3));
        mDelView.setTextSize(18);
        mDelView.setGravity(Gravity.CENTER);
        mDelView.setVisibility(View.GONE);
        mDelView.setText("删除");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZYScreenUtils.dp2px(mActivity, 50));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mDelView.setLayoutParams(layoutParams);
        mRootView.addView(mDelView);
    }

    void showDel() {
        mDelView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).getLayoutParams();
        layoutParams.bottomMargin = ZYScreenUtils.dp2px(mActivity, 50);
        ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setLayoutParams(layoutParams);

    }

    void hideDel() {
        mDelView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).getLayoutParams();
        layoutParams.bottomMargin = ZYScreenUtils.dp2px(mActivity, 0);
        ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setLayoutParams(layoutParams);
    }

    @Override
    protected ZYBaseViewHolder<SRCatalogueNew> createViewHolder() {
        return new SRCataloguesVH();
    }

    @Override
    public void delCatalogueSuc(SRCatalogueNew data) {
        showToast("作品删除成功!");
        mAdapter.notifyDataSetChanged();
    }

    public void setEdit(boolean isEditing) {
        mPresenter.setEdit(isEditing);
        mAdapter.notifyDataSetChanged();
        if (isEditing) {
            showDel();
        } else {
            hideDel();
        }
    }

    public void onDelClick(final SRCatalogueNew data) {
        new AlertDialog.Builder(mActivity).setTitle("删除").setMessage("是否删除该作品?")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delCatalogues(data);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }
}
