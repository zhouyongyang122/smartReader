package com.smartreader.ui.profile.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartreader.R;
import com.smartreader.base.activity.ZYEditDescActivity;
import com.smartreader.base.mvp.ZYBaseFragment;
import com.smartreader.base.view.ZYPicSelect;
import com.smartreader.base.view.ZYWheelSelectDialog;
import com.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.smartreader.ui.login.model.SRUserManager;
import com.smartreader.ui.profile.contact.SREditContract;
import com.smartreader.ui.profile.model.bean.SRUserParams;
import com.smartreader.utils.ZYToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/4/7.
 */

public class SREditFragment extends ZYBaseFragment<SREditContract.IPresenter> implements SREditContract.IView, ZYPicSelect.PicSelectListener {

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.textName)
    TextView textName;

    @Bind(R.id.textSchool)
    TextView textSchool;

    @Bind(R.id.textGrade)
    TextView textGrade;

    @Bind(R.id.textAge)
    TextView textAge;

    @Bind(R.id.textGender)
    TextView textGender;

    SRUserParams userParams;

    ZYPicSelect picSelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sr_fragment_edit, container, false);
        ButterKnife.bind(this, view);
        userParams = SRUserParams.getDefUserParams();

        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, userParams.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
        textName.setText(userParams.nickname);
        textSchool.setText(userParams.school);
        textGrade.setText(userParams.grade <= 0 ? "还没有设置年级" : userParams.grade + "年级");
        textAge.setText(userParams.age <= 0 ? "还没有设置年龄" : userParams.age + "岁");
        textGender.setText(userParams.sex <= 0 ? "还没有设置性别" : (userParams.sex == 1 ? "男" : "女"));
        return view;
    }

    @OnClick({R.id.imgAvatar, R.id.textName, R.id.textSchool, R.id.textGrade, R.id.textAge, R.id.textGender, R.id.textOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAvatar:
                if (picSelect == null) {
                    picSelect = new ZYPicSelect(mActivity, this);
                }
                break;
            case R.id.textName:
                mActivity.startActivityForResult(ZYEditDescActivity.createIntent(mActivity, 16, SRUserManager.getInstance().getUser().nickname, "修改昵称", "输入昵称(最长16个字)"), 100);
                break;
            case R.id.textSchool:
                mActivity.startActivityForResult(ZYEditDescActivity.createIntent(mActivity, 16, SRUserManager.getInstance().getUser().school, "修改昵称", "输入学校名称(最长16个字)"), 200);
                break;
            case R.id.textGrade:
                seletctGrade();
                break;
            case R.id.textAge:
                seletctAget();
                break;
            case R.id.textGender:
                seletctGender();
                break;
            case R.id.textOk:
                String tipMsg = userParams.checkParams();
                if (tipMsg != null) {
                    ZYToast.show(mActivity, tipMsg);
                    return;
                }
                mPresenter.editUser(userParams.getParamas());
                break;
        }
    }

    private void seletctGrade() {
        String[] grades = new String[]{"1年级", "2年级", "3年级", "4年级", "5年级", "6年级", "7年级", "8年级", "9年级", "10年级", "11年级", "11年级"};
        ZYWheelSelectDialog wheelSelectDialog = new ZYWheelSelectDialog(mActivity, grades, new ZYWheelSelectDialog.WheelSelectListener() {
            @Override
            public void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value) {
                userParams.grade = position + 1;
                textGrade.setText(userParams.grade + "年级");
            }
        });
        wheelSelectDialog.showDialog(0);
    }

    private void seletctGender() {
        String[] grades = new String[]{"男生", "女生"};
        ZYWheelSelectDialog wheelSelectDialog = new ZYWheelSelectDialog(mActivity, grades, new ZYWheelSelectDialog.WheelSelectListener() {
            @Override
            public void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value) {
                userParams.sex = position + 1;
                textGender.setText(userParams.sex <= 0 ? "还没有设置性别" : (userParams.sex == 1 ? "男" : "女"));
            }
        });
        wheelSelectDialog.showDialog(0);
    }

    private void seletctAget() {
        final String[] ages = new String[58];
        for (int i = 3; i <= 60; i++) {
            ages[i] = i + "岁";
        }
        ZYWheelSelectDialog wheelSelectDialog = new ZYWheelSelectDialog(mActivity, ages, new ZYWheelSelectDialog.WheelSelectListener() {
            @Override
            public void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value) {
                userParams.age = position + 3;
                textAge.setText(userParams.age + "岁");
            }
        });
        wheelSelectDialog.showDialog(0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ZYEditDescActivity.RESULT_CODE_OK) {
            String value = data.getStringExtra(ZYEditDescActivity.RESULT_VALUE);
            if (requestCode == 100) {
                userParams.nickname = value;
                textName.setText(userParams.nickname);
            } else {
                userParams.school = value;
                textSchool.setText(userParams.school);
            }
        } else {
            if (picSelect != null) {
                picSelect.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onPicSelected(Uri uri) {
        userParams.avatar = uri.getPath();
        ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, userParams.avatar, R.drawable.def_avatar, R.drawable.def_avatar);
    }
}
