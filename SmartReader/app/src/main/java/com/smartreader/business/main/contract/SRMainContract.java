package com.smartreader.business.main.contract;

import com.smartreader.common.mvp.SRIBasePresenter;
import com.smartreader.common.mvp.SRIBaseView;

/**
 * Created by ZY on 17/3/16.
 */

public interface SRMainContract {

    interface IView extends SRIBaseView<IPresenter>{

    }

    interface IPresenter extends SRIBasePresenter{

    }
}
