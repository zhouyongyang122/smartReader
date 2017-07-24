package com.qudiandu.smartreader.ui.main.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.event.SREventSelectedBook;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.base.view.ZYRefreshListener;
import com.qudiandu.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.activity.SRCreateClassActivity;
import com.qudiandu.smartreader.ui.main.activity.SRGradeActivity;
import com.qudiandu.smartreader.ui.main.activity.SRJoinClassActivity;
import com.qudiandu.smartreader.ui.main.contract.SRClassContract;
import com.qudiandu.smartreader.ui.main.model.SRAddBookManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.main.model.bean.SRTaskTitle;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassChoseIdentityVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassListVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassTaskFinishItemVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassTaskItemVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassTaskTitle;
import com.qudiandu.smartreader.ui.mark.activity.SRMarkActivity;
import com.qudiandu.smartreader.ui.task.model.SRTaskManager;
import com.qudiandu.smartreader.utils.ZYStatusBarUtils;
import com.qudiandu.smartreader.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/22.
 */

public class SRClassFragment extends ZYBaseFragment<SRClassContract.IPresenter> implements SRClassContract.IView, SRClassChoseIdentityVH.ClassChoseIdentityListener, SRClassListVH.ClassListListener, SRClassTaskItemVH.ClassTaskItemListener {

    final int TITLE_TYPE = 0;

    final int TASK_TYPE = 1;

    final int TASK_FINISH_TYPE = 2;

    ViewGroup rootView = null;

    @Bind(R.id.layoutProgressBar)
    LinearLayout layoutProgressBar;
    @Bind(R.id.textWait)
    TextView textWait;

    @Bind(R.id.layoutClassRoot)
    LinearLayout layoutClassRoot;

    @Bind(R.id.layoutAction)
    LinearLayout layoutAction;

    @Bind(R.id.imgClassDetail)
    ImageView imgClassDetail;

    @Bind(R.id.imgClassAdd)
    ImageView imgClassAdd;

    @Bind(R.id.textTitle)
    TextView textTitle;
    @Bind(R.id.imgArrow)
    ImageView imgArrow;

    @Bind(R.id.textAdd)
    TextView textAdd;

    @Bind(R.id.sRecyclerView)
    ZYSwipeRefreshRecyclerView sRecyclerView;

    ZYBaseRecyclerAdapter<Object> adapter;

    SRClassChoseIdentityVH choseIdentityVH;

    SRClassListVH classListVH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.sr_fragment_home_class, container, false);
        rootView.setPadding(0, ZYStatusBarUtils.getStatusBarHeight(mActivity), 0, 0);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (SRUserManager.getInstance().isGuesterUser(false) || SRUserManager.getInstance().getUser().isNoIdentity()) {
                choseIdentityVH = new SRClassChoseIdentityVH(this);
                choseIdentityVH.attachTo(rootView);
            }
        }
    }

    private void initView() {
        sRecyclerView.getLoadingView().setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.subscribe();
            }
        });
        sRecyclerView.setRefreshListener(new ZYRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTasks(true);
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadTasks(false);
            }
        });

        adapter = new ZYBaseRecyclerAdapter<Object>(mPresenter.getData()) {
            @Override
            public ZYBaseViewHolder<Object> createViewHolder(int type) {
                if (type == TITLE_TYPE) {
                    return new SRClassTaskTitle();
                } else if (type == TASK_TYPE) {
                    return new SRClassTaskItemVH(SRUserManager.getInstance().getUser().isTeacher(), SRClassFragment.this);
                }
                return new SRClassTaskFinishItemVH();
            }

            @Override
            public int getItemViewType(int position) {
                if (SRUserManager.getInstance().getUser().type == SRUser.STUDY_TYPE) {
                    if (adapter.getItem(position) instanceof SRTaskTitle) {
                        return TITLE_TYPE;
                    }
                    SRTask task = (SRTask) adapter.getItem(position);
                    if (task.finish != null && task.finish.size() > 0) {
                        return TASK_FINISH_TYPE;
                    }
                    return TASK_TYPE;
                } else if (SRUserManager.getInstance().getUser().type == SRUser.TEACHER_TYPE) {
                    if (adapter.getItem(position) instanceof SRTaskTitle) {
                        return TITLE_TYPE;
                    } else {
                        return TASK_TYPE;
                    }
                }
                return super.getItemViewType(position);
            }
        };
        adapter.setOnItemClickListener(new ZYBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        sRecyclerView.setAdapter(adapter);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        classListVH = new SRClassListVH(this);
        classListVH.attachTo(layoutClassRoot);
    }

    @OnClick({R.id.textTitle, R.id.imgClassDetail, R.id.imgClassAdd, R.id.textAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textAdd:
                if (SRUserManager.getInstance().getUser().type == SRUser.STUDY_TYPE) {
                    //加入班级
                    mActivity.startActivity(SRJoinClassActivity.createIntent(mActivity));
                } else {
                    //添加作业
                    SRClass srClass = mPresenter.getClasses().get(mPresenter.getCurrentClassPosition());
                    SRTaskManager.getInstance().setCurrentTaskClassId(srClass.group_id);
                    mActivity.startActivity(SRGradeActivity.createIntent(mActivity, true));
                }
                break;
            case R.id.imgClassDetail:
                //班级详情
                break;
            case R.id.imgClassAdd:
                //新增班级
                mActivity.startActivity(SRCreateClassActivity.createIntent(mActivity));
                break;
            case R.id.textTitle:
                if (classListVH.isvisiable()) {
                    imgArrow.setSelected(false);
                    classListVH.hide();
                } else {
                    imgArrow.setSelected(true);
                    classListVH.updateView(mPresenter.getClasses(), 0);
                }
                break;
        }
    }

    @Override
    public void onStudentClick() {
        mPresenter.updateIdentity(SRUser.STUDY_TYPE);
    }

    @Override
    public void onTeacherClick() {
        mPresenter.updateIdentity(SRUser.TEACHER_TYPE);
    }

    @Override
    public void showList(boolean isHasMore) {
        sRecyclerView.showList(isHasMore);
    }

    @Override
    public void showEmpty() {
        sRecyclerView.showEmpty();
    }

    @Override
    public void showLoading() {
        sRecyclerView.showLoading();
        layoutProgressBar.setVisibility(View.VISIBLE);
        layoutAction.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        sRecyclerView.showEmpty();
    }

    @Override
    public void showError() {
        sRecyclerView.showError();
    }

    @Override
    public void choseIdentitySuc() {
        choseIdentityVH.unAttach();
        if (SRUserManager.getInstance().getUser().type == SRUser.STUDY_TYPE) {
            //加入班级
            mActivity.startActivity(SRJoinClassActivity.createIntent(mActivity));
        } else {
            //创建班级
            mActivity.startActivity(SRCreateClassActivity.createIntent(mActivity));
        }
    }

    @Override
    public void refreshClasses() {
        layoutProgressBar.setVisibility(View.GONE);
        layoutAction.setVisibility(View.VISIBLE);
        if (SRUserManager.getInstance().getUser().type == SRUser.STUDY_TYPE) {
            imgClassAdd.setVisibility(View.GONE);
            textAdd.setText("添加班级");
            mActivity.startActivity(SRJoinClassActivity.createIntent(mActivity));
        } else {
            imgClassAdd.setVisibility(View.VISIBLE);
            textAdd.setText("添加作业");
        }
    }

    @Override
    public void showClassEmpty() {
        ZYLoadingView loadingView = (ZYLoadingView) sRecyclerView.getLoadingView();
        if (SRUserManager.getInstance().getUser().type == SRUser.STUDY_TYPE) {
            textWait.setText("还有没加入班级");
            loadingView.showEmptyBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //加入班级
                    mActivity.startActivity(SRJoinClassActivity.createIntent(mActivity));
                }
            }, "点击加入班级");
        } else {
            textWait.setText("还有没创建班级");
            loadingView.showEmptyBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //创建班级
                    mActivity.startActivity(SRCreateClassActivity.createIntent(mActivity));
                }
            }, "点击创建班级");
        }
    }

    @Override
    public void showClassTaskEmpty() {
        sRecyclerView.showEmpty();
    }

    @Override
    public void onClassSelecte(SRClass value, int positon) {
        classListVH.hide();
        mPresenter.setSelectClass(positon);
        textTitle.setText(value.class_name);
        imgArrow.setSelected(false);
    }

    @Override
    public void onFinisheTask(final SRTask task) {
        SRBook book = SRBook.queryById(task.book_id + "");
        if (book != null) {
            if (book.isFinished()) {
                //去完成任务

            } else {
                ZYToast.show(mActivity, "任务相关的课本正在下载队列中,请回首页查看!");
            }
        } else {
            new AlertDialog.Builder(mActivity).setTitle("下载课本").setMessage("完成任务需要先下载对应的课本").setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SRAddBookManager.getInstance().addBook(task.getBook());
                    EventBus.getDefault().post(new SREventSelectedBook());
                    ZYToast.show(mActivity, "课本正在下载中,请下载完成后再来完成任务!");
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }
    }

    @Override
    public void toFinishTask(SRTask task, ArrayList<SRTract> tracts) {
        mActivity.startActivity(SRMarkActivity.createIntent(mActivity, tracts, task.book_id + "",task.catalogue_id + "",task.group_id + "",task.task_id + ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mPresenter.isRefreshing()) {
            mPresenter.refreshClasss();
        }
    }
}
