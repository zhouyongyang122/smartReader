package com.qudiandu.smartreader.ui.invite;

import com.qudiandu.smartreader.base.mvp.ZYListDataContract;

/**
 * @作者 zhouyongyang$
 * @创建日期 2018/10/14$ 下午3:57$
 */
public interface SRInviteInfoContract {

    interface IView extends ZYListDataContract.View<IPresenter>{

    }

    interface IPresenter extends ZYListDataContract.Presenter<SRInviteInfo>{}

}
