package com.qudiandu.smartreader.ui.myAudio.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.qudiandu.smartreader.base.mvp.ZYListDateFragment;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.main.activity.SRMainActivity;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.myAudio.contract.SRCataloguesContract;
import com.qudiandu.smartreader.ui.myAudio.model.SRCatalogueNew;
import com.qudiandu.smartreader.ui.myAudio.view.viewHolder.SRCataloguesVH;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ZY on 17/6/10.
 */

public class SRCataloguesFragment extends ZYListDateFragment<SRCataloguesContract.IPresenter, SRCatalogueNew> implements SRCataloguesContract.IView, SRCataloguesVH.OnCataloguesListener {

    @Override
    protected void onItemClick(View view, int position) {
        SRCatalogueNew catalogueNew = mAdapter.getItem(position);
        if (catalogueNew != null) {
            mActivity.startActivity(SRCatalogueDetailActivity.createIntent(mActivity, catalogueNew.getId() + ""));
        }
    }

    @Override
    protected ZYBaseViewHolder<SRCatalogueNew> createViewHolder() {
        return new SRCataloguesVH(this);
    }

    @Override
    public void delCatalogueSuc(SRCatalogueNew data) {
        showToast("作品删除成功!");
        mAdapter.notifyDataSetChanged();
    }

    public void setEdit(boolean isEditing) {
        mPresenter.setEdit(isEditing);
        mAdapter.notifyDataSetChanged();
    }

    @Override
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
