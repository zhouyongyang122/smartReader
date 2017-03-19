package com.smartreader.ui.main.contract;

import com.smartreader.base.mvp.SRIBasePresenter;
import com.smartreader.base.mvp.SRIBaseView;

/**
 * Created by ZY on 17/3/16.
 */

public interface SRMainContract {

    interface IView extends SRIBaseView<IPresenter>{

    }

    interface IPresenter extends SRIBasePresenter{

    }
}
