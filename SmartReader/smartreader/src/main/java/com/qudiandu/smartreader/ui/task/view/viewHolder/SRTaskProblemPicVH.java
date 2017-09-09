package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
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
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgA, data.answer_pic.A, ZYScreenUtils.dp2px(mContext, 4));
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgB, data.answer_pic.B, ZYScreenUtils.dp2px(mContext, 4));
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgC, data.answer_pic.C, ZYScreenUtils.dp2px(mContext, 4));
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgD, data.answer_pic.D, ZYScreenUtils.dp2px(mContext, 4));
            if (SRUserManager.getInstance().getUser().isTeacher()) {
                refreshSelImg(mData.answer);
            }
        }
    }

    @OnClick({R.id.layoutA, R.id.layoutB, R.id.layoutC, R.id.layoutD})
    public void onClick(View view) {
        if (SRUserManager.getInstance().getUser().isTeacher()) {
            return;
        }
        switch (view.getId()) {
            case R.id.layoutA:
                refreshSelImg("A");
                mListener.onAnswerSelect("A");
                break;
            case R.id.layoutB:
                refreshSelImg("B");
                mListener.onAnswerSelect("B");
                break;
            case R.id.layoutC:
                refreshSelImg("C");
                mListener.onAnswerSelect("C");
                break;
            case R.id.layoutD:
                refreshSelImg("D");
                mListener.onAnswerSelect("D");
                break;
        }


    }

    private void refreshSelImg(String answer) {
        int selIndex = 0;
        if (answer.equals("A")) {
            selIndex = 0;
        } else if (answer.equals("B")) {
            selIndex = 1;
        } else if (answer.equals("C")) {
            selIndex = 2;
        } else if (answer.equals("D")) {
            selIndex = 3;
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
