package com.qudiandu.smartreader.ui.main.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.base.adapter.ZYBaseRecyclerAdapter;
import com.qudiandu.smartreader.base.mvp.ZYBaseFragment;
import com.qudiandu.smartreader.base.view.ZYLoadingView;
import com.qudiandu.smartreader.base.view.ZYOptionPicker;
import com.qudiandu.smartreader.base.view.ZYRefreshListener;
import com.qudiandu.smartreader.base.view.ZYSwipeRefreshRecyclerView;
import com.qudiandu.smartreader.base.viewHolder.ZYBaseViewHolder;
import com.qudiandu.smartreader.service.downNet.down.ZYDownloadManager;
import com.qudiandu.smartreader.ui.dubbing.activity.SRDubbingActivity;
import com.qudiandu.smartreader.ui.login.model.SRUserManager;
import com.qudiandu.smartreader.ui.login.model.bean.SRUser;
import com.qudiandu.smartreader.ui.main.activity.SRCreateClassActivity;
import com.qudiandu.smartreader.ui.main.activity.SRGradeActivity;
import com.qudiandu.smartreader.ui.main.activity.SRJoinClassActivity;
import com.qudiandu.smartreader.ui.main.contract.SRClassContract;
import com.qudiandu.smartreader.ui.main.model.SRBookFileManager;
import com.qudiandu.smartreader.ui.main.model.bean.SRBook;
import com.qudiandu.smartreader.ui.main.model.bean.SRClass;
import com.qudiandu.smartreader.ui.main.model.bean.SRTask;
import com.qudiandu.smartreader.ui.main.model.bean.SRTaskTitle;
import com.qudiandu.smartreader.ui.main.model.bean.SRTract;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassChoseIdentityVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassDetailHeaderVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassTaskItemVH;
import com.qudiandu.smartreader.ui.main.view.viewhodler.SRClassTaskTitleVH;
import com.qudiandu.smartreader.ui.mark.activity.SRMarkActivity;
import com.qudiandu.smartreader.ui.myAudio.activity.SRCatalogueDetailActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskCommentedActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskDetailActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskListenActivity;
import com.qudiandu.smartreader.ui.task.activity.SRTaskProblemActivity;
import com.qudiandu.smartreader.ui.task.model.SRTaskManager;
import com.qudiandu.smartreader.utils.ZYScreenUtils;
import com.qudiandu.smartreader.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by ZY on 17/7/22.
 */

public class SRClassFragment extends ZYBaseFragment<SRClassContract.IPresenter> implements SRClassContract.IView,
        SRClassChoseIdentityVH.ClassChoseIdentityListener,
        SRClassTaskItemVH.ClassTaskItemListener,
        SRClassTaskTitleVH.ClassTaskTitleListener {

    final int TITLE_TYPE = 0;

    final int TASK_TYPE = 1;

    final int TASK_FINISH_TYPE = 2;

    ViewGroup rootView = null;

    @Bind(R.id.layoutProgressBar)
    LinearLayout layoutProgressBar;
    @Bind(R.id.textWait)
    TextView textWait;

    @Bind(R.id.layoutAction)
    LinearLayout layoutAction;

    @Bind(R.id.textCreate)
    TextView textCreate;

    @Bind(R.id.textTitle)
    TextView textTitle;

    @Bind(R.id.textAdd)
    TextView textAdd;

    @Bind(R.id.layoutHeader)
    LinearLayout layoutHeader;

    @Bind(R.id.sRecyclerView)
    ZYSwipeRefreshRecyclerView sRecyclerView;

    ZYBaseRecyclerAdapter<Object> adapter;

    SRClassDetailHeaderVH detailHeaderVH;

    SRClassChoseIdentityVH choseIdentityVH;

    private boolean mIsEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.sr_fragment_home_class, container, false);
//        rootView.setPadding(0, ZYStatusBarUtils.getStatusBarHeight(mActivity), 0, 0);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (SRUserManager.getInstance().isGuesterUser(false) || SRUserManager.getInstance().getUser().isNoIdentity()) {
                showChoseIdentityVH();
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

        ZYLoadingView loadingView = (ZYLoadingView) sRecyclerView.getLoadingView();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loadingView.getView().getLayoutParams();
        layoutParams.height = ZYScreenUtils.getScreenHeight(mActivity) - ZYScreenUtils.dp2px(mActivity, 200);
        loadingView.getView().setLayoutParams(layoutParams);
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
                    return new SRClassTaskTitleVH(SRClassFragment.this);
                } else if (type == TASK_TYPE) {
                    return new SRClassTaskItemVH(SRClassFragment.this);
                }
                return new SRClassTaskItemVH(SRClassFragment.this);
            }

            @Override
            public int getItemViewType(int position) {
                if (super.getItemViewType(position) == ZYBaseRecyclerAdapter.TYPE_NORMAL) {
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
                Object object = adapter.getItem(position);
                if (object instanceof SRTask) {
                    SRTask task = (SRTask) object;
                    if (SRUserManager.getInstance().getUser().isTeacher()) {
                        if (task.isEdit) {
                            mPresenter.setDelTask(task);
                            new AlertDialog.Builder(mActivity).setTitle("删除").setMessage("是否删除任务?")
                                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mPresenter.delTask();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create().show();
                            return;
                        }
                        mActivity.startActivity(SRTaskDetailActivity.createIntent(mActivity, task));
                    } else {

                        if (task.ctype == SRTask.TASK_TYPE_LISTEN) {
                            onFinisheTask(task);
                            return;
                        }

                        if (task.finish != null && task.finish.size() > 0) {
                            if (!TextUtils.isEmpty(task.finish.get(0).comment)) {
                                startActivity(SRTaskCommentedActivity.createIntent(mActivity, task));
                                return;
                            } else {
                                mActivity.startActivity(SRCatalogueDetailActivity.createIntent(mActivity, task.finish.get(0).show_id + ""));
                            }
                            return;
                        }
                        onFinisheTask(task);
                    }
                }
            }
        });

        detailHeaderVH = new SRClassDetailHeaderVH(new SRClassDetailHeaderVH.ClassDetailHeaderListener() {
            @Override
            public void onClassChangeClick() {
                new ZYOptionPicker(mActivity, mPresenter.getClassNames(), new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int positon, String item) {
                        mPresenter.setSelectClass(positon);
                        textTitle.setText(item);
                        detailHeaderVH.updateView(mPresenter.getCurrentClass(), 0);
                    }
                }).show();
            }
        });
        detailHeaderVH.attachTo(layoutHeader);

        sRecyclerView.setAdapter(adapter);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

    }

    @OnClick({R.id.textCreate, R.id.textAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textAdd:
                if (SRUserManager.getInstance().getUser().isStudent()) {
                    //加入班级
                    mActivity.startActivity(SRJoinClassActivity.createIntent(mActivity));
                } else {
                    //添加作业
                    SRClass srClass = mPresenter.getCurrentClass();
                    SRTaskManager.getInstance().setCurrentTaskClassId(srClass.group_id);
                    mActivity.startActivity(SRGradeActivity.createIntent(mActivity, true));
                }
                break;
            case R.id.textCreate:
                //新增班级
                mActivity.startActivity(SRCreateClassActivity.createIntent(mActivity));
                break;
        }
    }

    @Override
    public void onStudentClick() {
        mPresenter.updateIdentity(SRUser.STUDY_TYPE, null);
    }

    @Override
    public void onTeacherClick() {
        mPresenter.updateIdentity(SRUser.TEACHER_TYPE, null);
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
        hideChoseIdentityVH();
        if (SRUserManager.getInstance().getUser().isStudent()) {
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
        if (SRUserManager.getInstance().getUser().isStudent()) {
            textCreate.setVisibility(View.INVISIBLE);
            textAdd.setText("加入班级");
        } else {
            textCreate.setVisibility(View.VISIBLE);
            textAdd.setText("任务布置");
        }
        SRClass srClass = mPresenter.getCurrentClass();
        textTitle.setText(srClass.class_name);
        detailHeaderVH.updateView(srClass, 0);
        detailHeaderVH.show();
    }

    @Override
    public void showClassEmpty() {
        ZYLoadingView loadingView = (ZYLoadingView) sRecyclerView.getLoadingView();
        layoutProgressBar.setVisibility(View.VISIBLE);
        layoutAction.setVisibility(View.GONE);
        if (SRUserManager.getInstance().getUser().isStudent()) {
            textWait.setText("我的班级");
            loadingView.setEmptyText("您还没有加入任何班级");
            loadingView.showEmptyBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //加入班级
                    mActivity.startActivity(SRJoinClassActivity.createIntent(mActivity));
                }
            }, "点击加入班级");
        } else {
            textWait.setText("我的班级");
            loadingView.setEmptyText("您还没有创建任何班级");
            loadingView.showEmptyBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //创建班级
                    mActivity.startActivity(SRCreateClassActivity.createIntent(mActivity));
                }
            }, "点击创建班级");
        }
        detailHeaderVH.hide();
    }

    @Override
    public void showClassTaskEmpty() {
        if (SRUserManager.getInstance().getUser().isStudent()) {
            sRecyclerView.getLoadingView().setEmptyText("老师还没有布置任务");
        } else {
            sRecyclerView.getLoadingView().setEmptyText("你还没有添加任务");
        }
        ZYLoadingView loadingView = (ZYLoadingView) sRecyclerView.getLoadingView();
        loadingView.hideEmptyBtn();
        loadingView.showEmpty();
    }


    @Override
    public void onFinisheTask(final SRTask task) {

        switch (task.ctype) {
            case SRTask.TASK_TYPE_RECORD: {
                SRBook book = SRBook.queryById(task.book_id + "");
                if (book != null) {
                    if (book.isFinished()) {
                        //去完成任务
                        mPresenter.toFinishTask(book.savePath, task);
                    } else {
                        ZYToast.show(mActivity, "任务相关的课本正在下载队列中,请回首页查看!");
                    }
                } else {
                    new AlertDialog.Builder(mActivity).setTitle("下载课本").setMessage("完成任务需要先下载对应的课本").setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SRBook downBook = task.getBook();
                            downBook.savePath = SRBookFileManager.getBookZipPath(downBook.book_id);
                            ZYDownloadManager.getInstance().addBook(downBook);
                            ZYToast.show(mActivity, "课本正在下载中,请下载完成后再来完成任务!");
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
            }
            break;
            case SRTask.TASK_TYPE_LISTEN:
                startActivity(SRTaskListenActivity.createIntent(mActivity, task.task_id));
                break;
            case SRTask.TASK_TYPE_PIC:
                startActivity(SRTaskProblemActivity.createIntent(mActivity, task.task_id, 0));
                break;
            case SRTask.TASK_TYPE_AUDIO:
                startActivity(SRTaskProblemActivity.createIntent(mActivity, task.task_id, 0));
                break;
        }
    }

    @Override
    public void toFinishTask(SRTask task, ArrayList<SRTract> tracts) {
        mActivity.startActivity(SRDubbingActivity.createIntent(mActivity, tracts, task.book_id + "", task.catalogue_id + "", task.group_id + "", task.task_id + "", ""));
    }

    @Override
    public void delTaskSuc() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskManagerClick(boolean isEdit) {
        for (Object object : mPresenter.getData()) {
            if (isEdit) {
                if (object instanceof SRTaskTitle) {
                    ((SRTaskTitle) object).isEdit = false;
                } else if (object instanceof SRTask) {
                    ((SRTask) object).isEdit = false;
                }
                mIsEdit = false;
            } else {
                if (object instanceof SRTaskTitle) {
                    ((SRTaskTitle) object).isEdit = true;
                } else if (object instanceof SRTask) {
                    ((SRTask) object).isEdit = true;
                }
                mIsEdit = true;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SRUserManager.getInstance().getUser().isNoIdentity()) {
            hideChoseIdentityVH();
        } else {
            showChoseIdentityVH();
        }
        if (!mPresenter.isRefreshing()) {
            mPresenter.refreshClasss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cancleManager();
    }

    void showChoseIdentityVH() {
        if (choseIdentityVH == null) {
            choseIdentityVH = new SRClassChoseIdentityVH(this);
            choseIdentityVH.attachTo(rootView);
        }
        choseIdentityVH.show();
    }

    void hideChoseIdentityVH() {
        if (choseIdentityVH != null) {
            choseIdentityVH.hide();
        }
    }

    public void cancleManager() {
        if (mIsEdit && adapter != null) {
            onTaskManagerClick(true);
        }
    }

    public boolean onBackPressed() {
        return true;
    }
}
