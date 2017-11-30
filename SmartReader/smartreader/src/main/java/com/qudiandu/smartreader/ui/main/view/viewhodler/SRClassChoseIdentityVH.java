package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/22.
 */

public class SRClassChoseIdentityVH extends ZYBaseViewHolder<Object> {

    @Bind(R.id.imgStudent)
    ImageView imgStudent;

    @Bind(R.id.imgTeacher)
    ImageView imgTeacher;

    @Bind(R.id.layoutSure)
    RelativeLayout layoutSure;

    ClassChoseIdentityListener listener;

    boolean isChoseTeacher;

    public SRClassChoseIdentityVH(ClassChoseIdentityListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.sr_view_class_chose_identity;
    }

    @OnClick({R.id.imgStudent, R.id.imgTeacher, R.id.layoutSure})
    public void onClick(View view) {
        if (SRUserManager.getInstance().isGuesterUser(true)) {
            return;
        }
        switch (view.getId()) {
            case R.id.imgTeacher:
                isChoseTeacher = true;
                imgStudent.setSelected(false);
                imgTeacher.setSelected(true);
                layoutSure.setVisibility(View.VISIBLE);
                break;
            case R.id.imgStudent:
                isChoseTeacher = false;
                imgStudent.setSelected(true);
                imgTeacher.setSelected(false);
                layoutSure.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutSure:
                if (isChoseTeacher) {
                    listener.onTeacherClick();
                } else {
                    listener.onStudentClick();
                }
                break;
        }
    }

    public interface ClassChoseIdentityListener {
        void onStudentClick();

        void onTeacherClick();
    }
}
