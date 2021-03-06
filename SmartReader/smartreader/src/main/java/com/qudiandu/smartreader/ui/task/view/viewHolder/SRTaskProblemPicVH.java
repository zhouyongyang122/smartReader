package com.qudiandu.smartreader.ui.task.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @Bind(R.id.textA)
    TextView textA;

    @Bind(R.id.layoutD)
    RelativeLayout layoutD;

    @Bind(R.id.imgD)
    ImageView imgD;

    @Bind(R.id.textD)
    TextView textD;

    @Bind(R.id.layoutC)
    RelativeLayout layoutC;

    @Bind(R.id.imgC)
    ImageView imgC;

    @Bind(R.id.textC)
    TextView textC;

    @Bind(R.id.layoutB)
    RelativeLayout layoutB;

    @Bind(R.id.imgB)
    ImageView imgB;

    @Bind(R.id.textB)
    TextView textB;

    @Bind(R.id.layoutLineTwo)
    LinearLayout layoutLineTwo;

    @Bind(R.id.layoutLineOne)
    LinearLayout layoutLineOne;

    SRTaskProblem.Problem mData;

    List<RelativeLayout> imgSels = new ArrayList<RelativeLayout>();
    List<TextView> imgIcons = new ArrayList<TextView>();

    TaskProblemPicListener mListener;

    public SRTaskProblemPicVH(TaskProblemPicListener listener) {
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        float scale = 172.0f / 110.0f;
        int with = (ZYScreenUtils.getScreenWidth(mContext) - ZYScreenUtils.dp2px(mContext, 45)) / 2;
        float height = (float) with / scale;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutLineOne.getLayoutParams();
        layoutParams.height = (int) height;
        layoutLineOne.setLayoutParams(layoutParams);

        layoutParams = (LinearLayout.LayoutParams) layoutLineTwo.getLayoutParams();
        layoutParams.height = (int) height;
        layoutLineTwo.setLayoutParams(layoutParams);

        imgSels.add(layoutA);
        imgIcons.add(textA);
        imgSels.add(layoutB);
        imgIcons.add(textB);
        imgSels.add(layoutC);
        imgIcons.add(textC);
        imgSels.add(layoutD);
        imgIcons.add(textD);
    }

    @Override
    public void updateView(SRTaskProblem.Problem data, int position) {
        if (data != null) {
            mData = data;
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgA, data.answer_pic.A, ZYScreenUtils.dp2px(mContext, 6));
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgB, data.answer_pic.B, ZYScreenUtils.dp2px(mContext, 6));
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgC, data.answer_pic.C, ZYScreenUtils.dp2px(mContext, 6));
            ZYImageLoadHelper.getImageLoader().loadRoundImage(this, imgD, data.answer_pic.D, ZYScreenUtils.dp2px(mContext, 6));
            if (SRUserManager.getInstance().getUser().isTeacher()) {
                refreshSelImg(mData.user_answer);
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
        for (RelativeLayout imageView : imgSels) {
            if (index == selIndex) {
                if (answer.equals(mData.answer)) {
                    imageView.setBackgroundResource(R.drawable.sr_bg_corner6dp_c13);
                    imgIcons.get(index).setBackgroundResource(R.drawable.chose_right_background);
                } else {
                    imageView.setBackgroundResource(R.drawable.sr_bg_corner6dp_c10);
                    imgIcons.get(index).setBackgroundResource(R.drawable.chose_right_worry_background);
                }
            } else {
                imageView.setBackgroundResource(R.drawable.sr_bg_corner6dp_c4_solid);
                imgIcons.get(index).setBackgroundResource(R.drawable.wait_chose_background);
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
