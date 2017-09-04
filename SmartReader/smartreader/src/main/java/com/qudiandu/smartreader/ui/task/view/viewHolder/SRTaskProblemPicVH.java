package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.task.model.bean.SRTaskProblem;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/9/4.
 */

public class SRTaskProblemPicVH extends ZYBaseViewHolder<SRTaskProblem.Problem> {

    @Bind(R.id.layoutA)
    RelativeLayout layoutA;

    @Bind(R.id.imgA)
    ImageView imgA;

    @Bind(R.id.imgASel)
    ImageView imgASel;

    @Bind(R.id.layoutD)
    RelativeLayout layoutD;

    @Bind(R.id.imgD)
    ImageView imgD;

    @Bind(R.id.imgDSel)
    ImageView imgDSel;

    @Bind(R.id.layoutC)
    RelativeLayout layoutC;

    @Bind(R.id.imgC)
    ImageView imgC;

    @Bind(R.id.imgCSel)
    ImageView imgCSel;

    @Bind(R.id.layoutB)
    RelativeLayout layoutB;

    @Bind(R.id.imgB)
    ImageView imgB;

    @Bind(R.id.imgBSel)
    ImageView imgBSel;

    SRTaskProblem.Problem mData;

    List<ImageView> imgSels = new ArrayList<ImageView>();

    TaskProblemPicListener mListener;

    public SRTaskProblemPicVH(TaskProblemPicListener listener) {
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        float scale = 354.0f / 153.0f;
        int with = ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 30);
        float height = (float) with / scale;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutA.getLayoutParams();
        layoutParams.height = (int) height;
        layoutA.setLayoutParams(layoutParams);

        layoutParams = (LinearLayout.LayoutParams) layoutB.getLayoutParams();
        layoutParams.height = (int) height;
        layoutB.setLayoutParams(layoutParams);

        layoutParams = (LinearLayout.LayoutParams) layoutC.getLayoutParams();
        layoutParams.height = (int) height;
        layoutC.setLayoutParams(layoutParams);

        layoutParams = (LinearLayout.LayoutParams) layoutD.getLayoutParams();
        layoutParams.height = (int) height;
        layoutD.setLayoutParams(layoutParams);

        imgSels.add(imgASel);
        imgSels.add(imgBSel);
        imgSels.add(imgCSel);
        imgSels.add(imgDSel);
    }

    @Override
    public void updateView(SRTaskProblem.Problem data, int position) {
        if (data != null) {
            mData = data;
        }
    }

    @OnClick({R.id.layoutA, R.id.layoutB, R.id.layoutC, R.id.layoutD})
    public void onClick(View view) {
        int selIndex = 0;
        switch (view.getId()) {
            case R.id.layoutA:
                selIndex = 0;
                mListener.onAnswerSelect("A");
                break;
            case R.id.layoutB:
                selIndex = 1;
                mListener.onAnswerSelect("B");
                break;
            case R.id.layoutC:
                selIndex = 2;
                mListener.onAnswerSelect("C");
                break;
            case R.id.layoutD:
                selIndex = 3;
                mListener.onAnswerSelect("D");
                break;
        }

        int index = 0;
        for (ImageView imageView : imgSels) {
            if (index == selIndex) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            index++;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_task_pic;
    }

    public interface TaskProblemPicListener {
        void onAnswerSelect(String answer);
    }
}
