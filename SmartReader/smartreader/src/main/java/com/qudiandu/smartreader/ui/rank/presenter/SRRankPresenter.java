package com.qudiandu.smartreader.ui.rank.presenter;

import com.qudiandu.smartreader.ZYPreferenceHelper;
import com.qudiandu.smartreader.base.bean.ZYResponse;
import com.qudiandu.smartreader.base.mvp.ZYBasePresenter;
import com.qudiandu.smartreader.base.mvp.ZYListDataPresenter;
import com.qudiandu.smartreader.service.net.ZYNetSubscriber;
import com.qudiandu.smartreader.service.net.ZYNetSubscription;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.rank.contract.SRRankContract;
import com.qudiandu.smartreader.ui.rank.model.SREventRefreshRankTop;
import com.qudiandu.smartreader.ui.rank.model.SRRankModel;
import com.qudiandu.smartreader.ui.rank.model.bean.SRRank;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ZY on 18/3/6.
 */

public class SRRankPresenter extends ZYListDataPresenter<SRRankContract.IView, SRRankModel, SRRank> implements SRRankContract.IPresenter {

    public static final int RANK_CLASS_TYPE = 2;//班级排行

    public static final int RANK_ALL_TYPE = 1;//全国排行

    public static final int TIME_DAY_TYPE = 1;

    public static final int TIME_WEEK_TYPE = 2;

    public int mRankType = RANK_ALL_TYPE;

    public int mTimeType = TIME_DAY_TYPE;

    public String mClassId;

    ArrayList<SRRank> mTops = new ArrayList<SRRank>();

    boolean mFristLoading = true;

    public SRRankPresenter(SRRankContract.IView view, int rank_type) {
        super(view, new SRRankModel());
        mRankType = rank_type;
        mClassId = ZYPreferenceHelper.getInstance().getClassId();
    }

    public void setTimeType(int timeType) {
        this.mTimeType = timeType;
        loadData();
    }

    @Override
    protected void loadData() {

        if (mRankType == RANK_ALL_TYPE) {
            mClassId = "";
        } else {
            mClassId = ZYPreferenceHelper.getInstance().getClassId();
        }

        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getRanks(mClassId, mRankType + "", mTimeType + "", mStart, mRows), new ZYNetSubscriber<ZYResponse<List<SRRank>>>() {

            @Override
            public void onSuccess(ZYResponse<List<SRRank>> response) {
                mFristLoading = false;
                List<SRRank> infos = new ArrayList<SRRank>();
                if (isRefresh()) {
                    mTops.clear();
                    for (SRRank rank : response.data) {
                        if (mTops.size() < 3) {
                            mTops.add(rank);
                        } else {
                            infos.add(rank);
                        }
                    }
                    EventBus.getDefault().post(new SREventRefreshRankTop(mRankType));
                } else {
                    infos.addAll(response.data);
                }

                success(infos);
            }

            @Override
            public void onFail(String message) {
                fail(message);
            }
        }));
    }

    @Override
    public ArrayList<SRRank> getTops() {
        return mTops;
    }

    public boolean isFristLoading() {
        return mFristLoading;
    }
}
