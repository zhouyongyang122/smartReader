package com.qudiandu.smartreader.ui.invite;

import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 上午11:00$
 */
public class SRInviteInfoPresenter extends ZYListDataPresenter<SRInviteInfoContract.IView, SRInviteModel, SRInviteInfo> implements SRInviteInfoContract.IPresenter {

    public SRInviteInfoPresenter(SRInviteInfoContract.IView view) {
        super(view, new SRInviteModel());
    }

    @Override
    protected void loadData() {

        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getInvites(mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRInviteInfo>>>() {

            @Override
            public void onSuccess(ZYResponse<List<SRInviteInfo>> response) {

                List<SRInviteInfo> results = new ArrayList<SRInviteInfo>();
                SRInviteInfo inviteInfo = new SRInviteInfo();
                inviteInfo.create_time = System.currentTimeMillis() / 1000;
                inviteInfo.info = "这就是测试数据1";
                inviteInfo.mobile = "17706532345";
                inviteInfo.remark = "这还是测试数据1";
                results.add(inviteInfo);

                inviteInfo = new SRInviteInfo();
                inviteInfo.create_time = System.currentTimeMillis() / 1000;
                inviteInfo.info = "这就是测试数据2";
                inviteInfo.mobile = "17706532345";
                inviteInfo.remark = "这还是测试数据2";
                results.add(inviteInfo);

                inviteInfo = new SRInviteInfo();
                inviteInfo.create_time = System.currentTimeMillis() / 1000;
                inviteInfo.info = "这就是测试数据3";
                inviteInfo.mobile = "17706532345";
                inviteInfo.remark = "这还是测试数据3";
                results.add(inviteInfo);

                response.data.addAll(results);

                success(response);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }
}
