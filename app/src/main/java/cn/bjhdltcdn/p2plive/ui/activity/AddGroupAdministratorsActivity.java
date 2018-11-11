package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.GroupOperationEvent;
import cn.bjhdltcdn.p2plive.event.UpdateManagersEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupUserListResponse;
import cn.bjhdltcdn.p2plive.model.GroupUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.AddGroupAdministratorsAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * 添加管理员和删除群成员
 */
public class AddGroupAdministratorsActivity extends BaseActivity implements BaseView {
    private AddGroupAdministratorsAdapter addAdministratorsAdapter;
    private long groupId;
    private List<Long> managerIdsList;
    private List<GroupUser> managerList = new ArrayList<>(1);
    private RecyclerView recyclerView;
    private int pageNumber = 1;
    private int pageSize = 20;
    private GroupPresenter groupPresenter;
    private TextView selectNumView;
    private ToolBarFragment titleFragment;
    // 刷新框架
    private TwinklingRefreshLayout refreshLayout;
    /**
     * 1,设置管理员 2删除群成员
     */
    private int type;

    public GroupPresenter getGroupPresenter() {
        if (groupPresenter == null) {
            groupPresenter = new GroupPresenter(this);
        }
        return groupPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_administrator);
        groupId = getIntent().getLongExtra(Constants.Fields.GROUP_ID, 0);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        initView();
        setTitle();
        requestData();
    }


    public void initView() {
        if (type == 1) {
            View upLineView = findViewById(R.id.view_line_up);
            upLineView.setVisibility(View.VISIBLE);
            selectNumView = findViewById(R.id.tv_num_select);
            selectNumView.setVisibility(View.VISIBLE);
            View downLineView = findViewById(R.id.view_line_down);
            downLineView.setVisibility(View.VISIBLE);
        }
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f)));
        addAdministratorsAdapter = new AddGroupAdministratorsAdapter(type, new AddGroupAdministratorsAdapter.OnItemClick() {
            @Override
            public void onItemClick(int selectNum) {
                selectNumView.setText("最多3个  " + selectNum + "/3");
            }
        });

        recyclerView.setAdapter(addAdministratorsAdapter);
        // 刷新框架
        refreshLayout = findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                requestData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                requestData();
            }
        });
    }

    /**
     * 请求数据
     */
    private void requestData() {
        getGroupPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), groupId, pageSize, pageNumber);
    }

    private void setTitle() {
        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        if (type == 1) {
            setTitleAndRight();
        } else {
            titleFragment.setTitleView("删除成员");
            titleFragment.setRightView("完成", R.color.color_ffb700,new ToolBarFragment.ViewOnclick() {
                @Override
                public void onClick() {
                    managerIdsList = addAdministratorsAdapter.getDelGroupUserIdsList();
                    if (managerIdsList.size() > 0) {
                        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                        getGroupPresenter().signOutGroup(groupId, userId, managerIdsList);
                    } else {
                        Utils.showToastShortTime("请选择要删除的成员");
                    }
                }
            });
        }
    }

    /**
     * 设置标题和右侧按钮
     */
    private void setTitleAndRight() {
        titleFragment.setTitleView("群管理员");
        addAdministratorsAdapter.setItemClickAble(false);
        titleFragment.setRightView("编辑", R.color.color_ffb700,new ToolBarFragment.ViewOnclick() {
            @Override
            public void onClick() {
                //添加管理员
                addAdministratorsAdapter.setItemClickAble(true);
                addAdministratorsAdapter.notifyDataSetChanged();
                titleFragment.setTitleView("添加管理员");
                titleFragment.setRightView("完成",R.color.color_ffb700,new ToolBarFragment.ViewOnclick() {
                    @Override
                    public void onClick() {
                        //添加管理员
                        managerIdsList = addAdministratorsAdapter.getManagerIdsList();
                        managerList = addAdministratorsAdapter.getManagerList();
                        getGroupPresenter().setManager(groupId, managerIdsList);
                    }
                });
            }
        });
    }


    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_SETMANAGER:
                if (object instanceof BaseResponse) {
                    BaseResponse setManagerResponse = (BaseResponse) object;
                    if (setManagerResponse.getCode() == 200) {
                        setTitleAndRight();
                        Utils.showToastShortTime(setManagerResponse.getMsg());
                        addAdministratorsAdapter.notifyDataSetChanged();
                        EventBus.getDefault().post(new UpdateManagersEvent());
                    }
                }
                break;
            case InterfaceUrl.URL_GETGROUPUSERLIST:
                if (object instanceof GetGroupUserListResponse) {

                    GetGroupUserListResponse response = (GetGroupUserListResponse) object;

                    if (response.getCode() == 200) {
                        // 设置群管理员
                        if (pageNumber == 1) {
                            addAdministratorsAdapter.getGroupUserList().clear();
                            managerList.clear();
                            addAdministratorsAdapter.setGroupUserList(response.getGroupUserList());
                            if (type == 1) {
                                for (GroupUser groupUser : response.getGroupUserList()) {
                                    if (groupUser.getUserRole() == 2) {
                                        managerList.add(groupUser);
                                    }
                                }
                                addAdministratorsAdapter.setMaxSelectNum(managerList.size());
                                selectNumView.setText("最多3个  " + managerList.size() + "/3");
                            }
                            addAdministratorsAdapter.notifyDataSetChanged();
                        } else {
                            addAdministratorsAdapter.addList(response.getGroupUserList());
                        }

                        if (response.getTotal() > addAdministratorsAdapter.getItemCount()) {// 减去最后item
                            pageNumber++;
                            refreshLayout.setEnableLoadmore(true);
                        }else{
                            refreshLayout.setEnableLoadmore(false);
                        }


                    } else {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }

                break;
            case InterfaceUrl.URL_SIGNOUTGROUP:
                if (object instanceof BaseResponse) {

                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        Utils.showToastShortTime("删除成功");
                        EventBus.getDefault().post(new UpdateManagersEvent());
                        finish();
                    }


                }
                break;

        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
