package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.view.View;
import android.widget.ImageView;

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

    ClassChoseIdentityListener listener;

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

    @OnClick({R.id.imgStudent, R.id.imgTeacher})
    public void onClick(View view) {
        if (SRUserManager.getInstance().isGuesterUser(true)) {
            return;
        }
        switch (view.getId()) {
            case R.id.imgTeacher:
                listener.onTeacherClick();
                break;
            case R.id.imgStudent:
                listener.onStudentClick();
                break;
        }
    }

    public interface ClassChoseIdentityListener {
        void onStudentClick();

        void onTeacherClick();
    }
}
