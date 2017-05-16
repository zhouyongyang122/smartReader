package com.qudiandu.smartreader.ui.mark.view;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qiniu.rs.CallBack;
import com.qiniu.rs.CallRet;
import com.qiniu.rs.UploadCallRet;
import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseRecyclerFragment;
import com.qudiandu.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.thirdParty.translate.TranslateRequest;
import com.qudiandu.smartreader.thirdParty.translate.YouDaoBean;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.mark.contract.SRMarkContract;
import com.qudiandu.smartreader.ui.mark.model.bean.SRMarkBean;
import com.qudiandu.smartreader.utils.SRShareUtils;
import com.qudiandu.smartreader.utils.ZYLog;
import com.qudiandu.smartreader.utils.ZYToast;
import com.qudiandu.smartreader.utils.ZYUtils;
import com.third.loginshare.entity.ShareEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ZY on 17/4/2.
 */

public class SRMarkFragment extends ZYBaseRecyclerFragment<SRMarkContract.IPresenter> implements SRMarkContract.IView, SRMarkItemVH.MarkItemListener {

    ZYBaseRecyclerAdapter<SRTract> adapter;

    SRTranslateVH translateVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        adapter = new ZYBaseRecyclerAdapter<SRTract>(mPresenter.getTracks()) {
            @Override
            public ZYBaseViewHolder<SRTract> createViewHolder(int type) {
                return new SRMarkItemVH(SRMarkFragment.this);
            }
        };

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ((ZYSwipeRefreshRecyclerView) mRefreshRecyclerView).setLayoutParams(params);

        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRefreshRecyclerView.setRefreshEnable(false);
        mRefreshRecyclerView.setLoadMoreEnable(false);
        mRefreshRecyclerView.setAdapter(adapter);

        mRefreshRecyclerView.showList(false);

        return view;
    }

    @Override
    public void uploadAudioSuc(SRMarkBean markBean) {
        share(markBean);
    }

    @Override
    public void audioUpload(final SRTract tract) {
        SRMarkBean markBean = tract.getMarkBean(mPresenter.getMarkId(tract.getTrack_id() + ""));
        if (markBean.share_url != null) {
            //已经上传成功,直接显示分享
            share(markBean);
        } else if (tract.audioQiNiuKey != null) {
            //音频已经上传成功,上传信息到服务器
            mPresenter.uploadTractAudio(tract);
        } else {
            //先上传音频到七牛
            File file = new File(markBean.audioPath);
            if (!file.exists()) {
                ZYToast.show(mActivity, "音频文件丢失,请重新录音!");
            } else {
                showProgress();

                String key = getTime() + File.separator + System.currentTimeMillis()
                        + SRUserManager.getInstance().getUser().uid + ".wav";

                ZYUtils.uploadFile(mActivity, key, markBean.audioPath, SRUserManager.getInstance().getUser().upload_token, new CallBack() {
                    @Override
                    public void onProcess(long current, long total) {

                    }

                    @Override
                    public void onSuccess(UploadCallRet ret) {
                        if (ret != null) {
                            try {
                                String picKey = ret.getKey();
                                ZYLog.e(SRMarkFragment.class.getSimpleName(), "uploadAudio-key: " + picKey);
                                tract.audioQiNiuKey = picKey;
                                mPresenter.uploadTractAudio(tract);
                            } catch (Exception e) {
                                hideProgress();
                                ZYToast.show(mActivity, e.getMessage() + "");
                            }
                        } else {
                            hideProgress();
                            ZYToast.show(mActivity, "上传失败,请重新录音,再重试");
                        }
                    }

                    @Override
                    public void onFailure(CallRet ret) {
                        hideProgress();
                        ZYToast.show(mActivity, "上传失败: " + ret.getStatusCode());
                    }
                });
            }
        }
    }

    private void share(SRMarkBean markBean) {
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.avatarUrl = SRUserManager.getInstance().getUser().avatar;
        if (TextUtils.isEmpty(shareEntity.avatarUrl)) {
            shareEntity.avatarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.def_avatar);
        }
        shareEntity.webUrl = markBean.share_url;
        shareEntity.title = "快来听听我的录音吧";
        shareEntity.text = "快来听听我的录音吧";
        new SRShareUtils(mActivity, shareEntity).share();
    }

    @Override
    public String getMarkId(String trackId) {
        return mPresenter.getMarkId(trackId);
    }

    @Override
    public void onShowMarkingItem(SRTract tract, int position) {
        List<SRTract> tracts = mPresenter.getTracks();
        int i = 0;
        for (SRTract value : tracts) {
            if (i != position && value.isRecordType) {
                value.isRecordType = false;
                value.isRecording = false;
                adapter.notifyItemChanged(i);
                break;
            }
            i++;
        }
    }

    @Override
    public void onTranslate(String word) {
        ZYLog.e(getClass().getSimpleName(), "translate: " + word);
        if (translateVH == null) {
            translateVH = new SRTranslateVH();
            translateVH.attachTo(mRootView);
        }
        translateVH.show(null);
        TranslateRequest.getRequest().translate(word, new TranslateRequest.TranslateRequestCallBack() {
            @Override
            public void translateCallBack(YouDaoBean translateBean, String errorMsg) {
                if (translateBean != null) {
                    translateVH.show(translateBean);
                } else {
                    ZYToast.show(mActivity, errorMsg == null ? "网络错误,请重试尝试!" : errorMsg);
                    translateVH.hide();
                }
            }
        });
    }

    public boolean onBackPressed() {
        if (translateVH != null && translateVH.isVisibility()) {
            translateVH.hide();
            return false;
        }
        return true;
    }

    @Override
    public void onMarkStart() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showWaitDialog("正在打分中....");
            }
        });
    }

    @Override
    public void onMarkEnd() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideWaitDialog();
            }
        });
    }

    @Override
    public void onMarkError(String msg) {
        ZYToast.show(mActivity, msg);
    }

    private String getTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
