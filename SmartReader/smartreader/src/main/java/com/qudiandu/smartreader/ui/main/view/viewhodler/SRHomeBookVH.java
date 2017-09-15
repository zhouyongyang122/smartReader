package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadManager;
import com.qudiandu.smartreader.ui.book.activity.SRBookUnitsActivity;
import com.qudiandu.smartreader.ui.book.activity.SRBooksActivity;
import com.qudiandu.smartreader.ui.book.view.viewHolder.SRBooksAddItemVH;
import com.qudiandu.smartreader.ui.book.view.viewHolder.SRBooksDefItemVH;
import com.qudiandu.smartreader.ui.book.view.viewHolder.SRBooksItemVH;
import com.qudiandu.smartreader.ui.main.activity.SRBookHomeActivity;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.utils.ZYFileUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/3/27.
 */

public class SRHomeBookVH extends ZYBaseViewHolder<SRBook> {

    @Bind(R.id.textTitle)
    TextView mTextTitle;

    @Bind(R.id.layoutAction)
    LinearLayout layoutAction;

    SRBook mData;

    @Override
    public void findView(View view) {
        super.findView(view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutAction.getLayoutParams();
        int width = (ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 45)) / 2;
        float scacle = 165.0f / 119.0f;
        layoutParams.height = (int) ((float) width / scacle);
        layoutAction.setLayoutParams(layoutParams);
    }

    @Override
    public void updateView(SRBook data, int position) {
        if (data != null) {
            mData = data;
            if (TextUtils.isEmpty(data.getGrade())) {
                data.setGrade("");
            }
            mTextTitle.setText(mData.getGrade() + "." + mData.getName());
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_home_book;
    }

    @OnClick({R.id.layoutBook, R.id.layoutRead, R.id.layoutRecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBook:
                mContext.startActivity(SRBooksActivity.createIntent(mContext, 0));
                break;
            case R.id.layoutRead:
                mContext.startActivity(SRBookHomeActivity.createIntent(mContext, mData.savePath));
                break;
            case R.id.layoutRecord:
                mContext.startActivity(SRBookUnitsActivity.createIntent(mContext, mData.savePath, mData.name));
                break;
        }
    }
}
