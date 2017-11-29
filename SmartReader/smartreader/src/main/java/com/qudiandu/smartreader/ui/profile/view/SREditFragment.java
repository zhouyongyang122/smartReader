package com.qudiandu.smartreader.ui.profile.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.activity.ZYEditDescActivity;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.view.ZYPicSelect;
import com.qudiandu.smartreader.base.view.ZYWheelSelectDialog;
import com.qudiandu.smartreader.thirdParty.image.ZYImageLoadHelper;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.profile.contact.SREditContract;
import com.qudiandu.smartreader.ui.profile.model.bean.SRUserParams;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Bind(R.id.textdentity)
    TextView textdentity;

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
        textSchool.setText(TextUtils.isEmpty(userParams.school) ? "还没有设置学校" : userParams.school);
        textGrade.setText(userParams.grade <= 0 ? "还没有设置年级" : userParams.grade + "年级");
        textAge.setText(userParams.age <= 0 ? "还没有设置年龄" : userParams.age + "岁");
        textGender.setText(userParams.sex <= 0 ? "还没有设置性别" : (userParams.sex == 1 ? "男" : "女"));
        textdentity.setText(userParams.identity <= 0 ? "还没有设置身份" : (userParams.identity == 2 ? "老师" : "学生"));
        return view;
    }

    @OnClick({R.id.textdentity, R.id.imgAvatar, R.id.textName, R.id.textSchool, R.id.textGrade, R.id.textAge, R.id.textGender, R.id.layoutOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAvatar:
                if (picSelect == null) {
                    picSelect = new ZYPicSelect(mActivity, this);
                }
                picSelect.showSelectDialog();
                break;
            case R.id.textName:
                mActivity.startActivityForResult(ZYEditDescActivity.createIntent(mActivity, 16, SRUserManager.getInstance().getUser().nickname, "修改昵称", "输入昵称(最长16个字)"), 100);
                break;
            case R.id.textSchool:
                mActivity.startActivityForResult(ZYEditDescActivity.createIntent(mActivity, 16, SRUserManager.getInstance().getUser().school, "修改学校", "输入学校名称(最长16个字)"), 200);
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
            case R.id.textdentity:
                selecteIdentity();
                break;
            case R.id.layoutOk:
                save();
                break;
        }
    }

    public void save() {
        String tipMsg = userParams.checkParams();
        if (tipMsg != null) {
            ZYToast.show(mActivity, tipMsg);
            return;
        }

        showProgress();
        if (userParams.avatar.startsWith("http")) {
            mPresenter.editUser(userParams.getParamas());
            return;
        }
        String key = getTime() + File.separator + System.currentTimeMillis()
                + SRUserManager.getInstance().getUser().uid + ".jpg";

        ZYUtils.uploadFile(mActivity, key, userParams.avatar, SRUserManager.getInstance().getUser().picture_token, new CallBack() {
            @Override
            public void onProcess(long current, long total) {

            }

            @Override
            public void onSuccess(UploadCallRet ret) {
                if (ret != null) {
                    try {
                        String picKey = ret.getKey();
                        userParams.qianniuKey = picKey;
                        mPresenter.editUser(userParams.getParamas());
                    } catch (Exception e) {
                        hideProgress();
                        ZYToast.show(mActivity, e.getMessage() + "");
                    }
                } else {
                    hideProgress();
                    ZYToast.show(mActivity, "上传失败,请重新选择头像,再重试");
                }
            }

            @Override
            public void onFailure(CallRet ret) {
                hideProgress();
                ZYToast.show(mActivity, "上传失败: " + ret.getStatusCode());
            }
        });
    }

    private void seletctGrade() {
        String[] grades = new String[]{"1年级", "2年级", "3年级", "4年级", "5年级", "6年级"};
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

    private void selecteIdentity() {
        String[] grades = new String[]{"学生", "老师"};
        ZYWheelSelectDialog wheelSelectDialog = new ZYWheelSelectDialog(mActivity, grades, new ZYWheelSelectDialog.WheelSelectListener() {
            @Override
            public void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value) {
                userParams.identity = position + 1;
                textdentity.setText(value);
            }
        });
        wheelSelectDialog.showDialog(0);
    }


    private void seletctAget() {
        final String[] ages = new String[58];
        for (int i = 3; i <= 60; i++) {
            ages[i - 3] = i + "岁";
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

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
